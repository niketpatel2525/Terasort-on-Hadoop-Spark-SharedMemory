package assigment.hadoop_demo;

import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.io.*;
import org.apache.hadoop.fs.Path;

public class Terasort {
	public static class TerasortMapper extends Mapper<Object, Text, Text, Text> {

		protected void map(Object key, Text value, Mapper<Object, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			String input = value.toString();

			String outkey = input.substring(0, 10);
			String outval = input.substring(10);
			context.write(new Text(outkey), new Text(outval));
		}

	}

	public static class TeraReducer extends Reducer<Text, Text, Text, Text> {

		protected void reduce(Text key, Iterable<Text> vals, Reducer<Text, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			Text outkey = key;
			Text outval = new Text();
			for (Text val : vals) {
				outval = val;
			}
			context.write(outkey, outval);
		}
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		long s1 = System.currentTimeMillis();
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "TeraSort on Hadoop i3.large");
		job.setJarByClass(Terasort.class);
		job.setMapperClass(TerasortMapper.class);
		job.setReducerClass(TeraReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setNumReduceTasks(100);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		if (job.waitForCompletion(true)) {
			long e1 = System.currentTimeMillis();
			double time = ((double) e1 - s1) / 1000.0;
			System.out.println("Total time to sort data on hadoop: " + time + " seconds");
		}
	}

}
