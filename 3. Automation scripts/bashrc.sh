echo "export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64" >> .bashrc
echo "export PATH=$PATH:$JAVA_HOME/bin" >> .bashrc
echo "export HADOOP_HOME=/data/hadoop" >> .bashrc
echo "export PATH=$PATH:$HADOOP_HOME/bin" >> .bashrc
echo "export PATH=$PATH:$HADOOP_HOME/sbin" >> .bashrc
echo "export HADOOP_COMMON_HOME=$HADOOP_HOME" >> .bashrc
echo "export HADOOP_HDFS_HOME=$HADOOP_HOME" >> .bashrc
echo "export HADOOP_MEPRED_HOME=$HADOOP_HOME" >> .bashrc
echo "export HADOOP_COMMON_LIB_NATIVE_DIR=$HADOOP_HOME/lib/native" >> .bashrc
echo "export YARN_HOME=$HADOOP_HOME" >> .bashrc
echo "export HADOOP_INSTALL=$HADOOP_HOME" >> .bashrc
echo "export HADOOP_OPTS=-Djava.library.path=$HADOOP_INSTALL/lib/native" >> .bashrc
echo "export HADOOP_CLASSPATH=/usr/lib/jvm/java-8-openjdk-amd64/lib/tools.jar" >> .bashrc

source .bashrc
