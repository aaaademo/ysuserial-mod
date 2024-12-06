#!/bin/bash

#设置临时环境变量JAVA_HOME，赋值为自己指定的jdk路径
export JAVA_HOME='/opt/Java/Java_8_202'

mvn install:install-file -Dfile=lib/tomcat-websocket-9.0.62.jar -DgroupId=org.apache.tomcat -DartifactId=tomcat-websocket -Dversion=9.0.62 -Dpackaging=jar
mvn install:install-file -Dfile=lib/resin.jar -DgroupId=com.caucho -DartifactId=resin -Dversion=4.0.65 -Dpackaging=jar
mvn install:install-file -Dfile=lib/javax.interceptor-api-3.1.jar -DgroupId=javax.interceptor -DartifactId=javax.interceptor-api -Dversion=3.1 -Dpackaging=jar
mvn install:install-file -Dfile=lib/javax.websocket-api-1.1.jar -DgroupId=javax.websocket -DartifactId=javax.websocket-api -Dversion=1.1 -Dpackaging=jar
mvn install:install-file -Dfile=lib/finereport/fine-third-10.0.jar -DgroupId=com.fr -DartifactId=fine-third -Dversion=10 -Dpackaging=jar
mvn install:install-file -Dfile=lib/ejb/res/org.mozilla.rhino_1.6.7.jar  -DgroupId=com.mozilla -DartifactId=rhino -Dversion=1.6.7 -Dpackaging=jar
mvn install:install-file -Dfile=lib/ejb/res/V9.5.2.4703.002.jar  -DgroupId=bes.ejb -DartifactId=sparkpatch -Dversion=V9.5.2.4703.002 -Dpackaging=jar

#调用mvn命令，将本脚本参数传过去
mvn $@

