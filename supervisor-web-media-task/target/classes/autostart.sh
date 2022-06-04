#java environment
export JAVA_HOME=/home/wsm/jdk/jdk1.8.0_221
export JRE_HOME=$JAVA_HOME/jre
export CLASSPATH=.:$JAVA_HOME/lib:$JRE_HOME/lib:$CLASSPATH
export PATH=$JAVA_HOME/bin:$JRE_HOME/bin:$PATH

nohup java -jar /home/wsm/boot-jar/jar/supervisor-web-media-task.jar >> /home/wsm/boot-jar/jar/supervisor-web-media-task-`date +'%Y-%m-%d'`.log 2>&1 &