FROM tomcat:9.0.82-jdk17-corretto-al2
COPY target/WalletService-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
CMD ["catalina.sh", "run"]
