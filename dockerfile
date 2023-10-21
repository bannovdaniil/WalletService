FROM tomcat:10
COPY target/WalletService-1.0-SNAPSHOT /usr/local/tomcat/webapps/ROOT
CMD ["catalina.sh", "run"]
