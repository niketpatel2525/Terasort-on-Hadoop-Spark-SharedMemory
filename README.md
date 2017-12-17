# Terasort-on-Hadoop-Spark-SharedMemory

================================================================================
                              Assignment Manual                              
================================================================================

Folder Structure
Assignment
 | --- 1.code
       | - Hadoop
       | - Shared Memory
       | - Spark
 | --- 2. Config
       | - Hadoop Config Files 1 Node
       | - Hadoop Config Files 8 Node
       | - Spark Config
 | --- 3. Automation Scripts
 | --- 4. Screenshots
 | --- 5. Report
   --- readme.txt

================================================================================
Shared Memory Terasort
================================================================================

Run java program as background service using following Command

$ javac ExternalSort.java
$ nohup java ExternalSort sample.txt &


================================================================================
Run Hadoop Terasort Program
================================================================================

I have attached jar file of Terasort program with this assignment. Use following
Command to run the program.



$ hadoop jar hadoop_demo-0.0.1-SNAPSHOT.jar  assigment.hadoop_demo.Terasort /input /output


================================================================================
Run Spark Terasort Program
================================================================================

To initiate Spark-shell, Use following command for respective configuration.

i3.large

$ spark-shell --conf spark.local.dir=/data/sparktmp --executor-memory 6g --num-executors 2 --driver-memory 4g

i3.4xlarge

$ spark-shell --conf spark.local.dir=/data/sparktmp --executor-memory 12g --num-executors 2 --driver-memory 4g
