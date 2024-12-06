
#!/bin/bash

#设置临时环境变量JAVA_HOME，赋值为自己指定的jdk路径
export JAVA_HOME='/opt/Java/Java_8_202'

#调用mvn命令，将本脚本参数传过去
mvn $@

