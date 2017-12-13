import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeMap;

public class ExternalSort {
	static String inputFile;
	static String tmp = "tmp/";

	public static void main(String[] args) throws Exception {
		inputFile = "sample.txt";
		System.out.println("Spliting Large File ..............");
		createChuncks();
	}

	private static void writeToFile(ArrayList<String> rows, int tmpfile) throws IOException {
		String filepath = tmp + "part" + tmpfile;
		FileWriter fw = new FileWriter(filepath);
		for (String s : rows) {
			fw.write(s);
			if (s.contains(" "))
				fw.write("\n");
		}
		fw.close();
	}

	private static long getBlockSize(File f) {
		System.gc();
		long freememory = Runtime.getRuntime().freeMemory();
		int maxsplit = 1024;
		long size = f.length();
		long blocksize = size / maxsplit;
		long breakEvenPoint = freememory / 2;
		System.out.println("Free Memory: " + freememory);
		if (blocksize < breakEvenPoint) {
			blocksize = freememory / 2;
		}
		return blocksize;
	}

	private static void createChuncks() throws IOException, InterruptedException {
		File f = new File(inputFile);
		long blocksize = getBlockSize(f);
		int tmpfile = 1;
		ArrayList<String> rows = null;

		BufferedReader reader = new BufferedReader(new FileReader(f));
		String line = "";
		while (line != null) {
			rows = new ArrayList<>();
			long currentblocksize = 0;
			line = reader.readLine();
			while (line != null && currentblocksize < blocksize) {
				rows.add(line);
				currentblocksize += line.length();
				line = reader.readLine();
			}
			writeToFile(rows, tmpfile);
			tmpfile++;
			rows = null;
		}
		reader.close();
		System.out.println("Small piece of chuncks created................");
		long l1 = System.currentTimeMillis();
		Master master = new Master();
		master.runMaster();
		long l2 = System.currentTimeMillis();
		System.out.println("Total Time: " + (l2 - l1) / 1000 + " seconds");

	}

	public static class FilePointer {
		boolean flagEmpty;
		static int BUFFERSIZE = 10240;
		String ptr;
		File f;
		public static BufferedReader bw;

		public FilePointer(File f) throws FileNotFoundException {
			bw = new BufferedReader(new FileReader(f), BUFFERSIZE);
			this.f = f;
		}

		private String readFile() throws IOException {
			try {
				ptr = bw.readLine();
				if (ptr == null) {
					flagEmpty = true;
					return null;
				}
			} catch (Exception e) {
				ptr = null;
				flagEmpty = true;
				return null;
			}
			return ptr;
		}

		private boolean isEmpty() {
			return flagEmpty;
		}

	}

	public static class Master implements Runnable {
		File[] tmpfiles;

		public Master() {

		}

		static Comparator<String> comparator = new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				// TODO Auto-generated method stub
				if (s1 != null && s2 != null)
					return s1.compareTo(s2);
				if (s1 == null) {
					return 1;
				} else
					return -1;
			}
		};

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				combineFiles();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void combineFiles() throws IOException {
			File out = new File("stmp/");
			File[] sortedFiles = out.listFiles();
			System.out.println("Combined Called");
			Queue<FilePointer> queue = new PriorityQueue<>(11, new Comparator<FilePointer>() {

				@Override
				public int compare(FilePointer o1, FilePointer o2) {
					// TODO Auto-generated method stub
					int b = 0;
					try {
						b = comparator.compare(o1.readFile(), o2.readFile());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return b;
				}
			});

			for (File f : sortedFiles) {
				FilePointer fp = new FilePointer(f);
				queue.add(fp);
			}
			File output = new File("output.txt");
			BufferedWriter bw = new BufferedWriter(new FileWriter(output));
			try {
				while (queue.size() > 0) {
					FilePointer fp = queue.poll();
					String l = fp.readFile();
					System.out.println(l);
					if (l != null && l.trim() != "") {
						bw.write(l + "\n");
					}
					if (fp.flagEmpty) {
						fp.bw.close();

					} else {
						queue.add(fp);
					}
				}
			} finally {
				bw.close();
				for (FilePointer fp : queue) {
					fp.bw.close();
				}
			}
		}

		public void runMaster() throws InterruptedException {
			// TODO Auto-generated method stub
			File f = new File("tmp/");
			tmpfiles = f.listFiles();
			System.out.println("Sorting process started..........");
			Thread t[] = new Thread[8];
			for (int i = 0; i < 1; i++) {
				t[i] = new Thread(new MergeSortWorker(tmpfiles));
				t[i].start();
			}
			for (int i = 0; i < 1; i++) {
				t[i] = new Thread(new MergeSortWorker(tmpfiles));
				t[i].join();
			}

		}

	}

	public static class MergeSortWorker implements Runnable {
		File[] tmpfiles;

		public MergeSortWorker(File[] tmpfiles) {
			// TODO Auto-generated constructor stub
			this.tmpfiles = tmpfiles;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			int nFile = tmpfiles.length;
			System.out.println("Total chunk of tmp files: " + nFile);
			System.out.println("Reading a file........");
			ArrayList<String> lines = null;
			for (int i = 0; i < tmpfiles.length; i++) {
				try {
					lines = readFromTmpFolder(i);
					writeSortedData(i, lines);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			System.out.println("Individual chuncks of data sorted.......");
			System.out.println("Merging is under progress........");
			Thread[] t = new Thread[8];
			for (int i = 0; i < 8; i++) {
				System.out.println("Thread#" + i + " started.....");
				t[i] = new Thread(new Master());
				t[i].start();
			}
			for (int i = 0; i < 8; i++) {
				try {
					t[i].join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("Files sorted and Merged......");
		}

		private ArrayList<String> readFromTmpFolder(int i) throws IOException {
			ArrayList<String> lines = new ArrayList<>();
			String line = "";
			BufferedReader br = new BufferedReader(new FileReader(tmpfiles[i]));
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
			ArrayList<String> sortedData = sortLines(i, lines);
			return sortedData;

		}

		private void writeSortedData(int i, ArrayList<String> lines) throws IOException {
			File f = new File("stmp/part" + i);
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			for (String s : lines) {
				bw.write(s);
				bw.newLine();
			}
			bw.flush();
			bw.close();
		}

		private ArrayList<String> sortLines(int i, ArrayList<String> lines) {
			ArrayList<String> sortedLines = null;

			Map<String, String> data = new TreeMap<>();
			for (String s : lines) {
				data.put(s.substring(0, 10), s.substring(10));
			}

			sortedLines = new ArrayList<>();
			for (Map.Entry<String, String> d : data.entrySet()) {
				sortedLines.add(d.getKey() + " " + d.getValue());
			}
			return sortedLines;
		}
	}

}
