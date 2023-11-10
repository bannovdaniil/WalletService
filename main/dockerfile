FROM amazoncorretto:17-alpine-jdk
COPY target/walletservice-*.jar walletservice.jar
ENTRYPOINT ["java","-jar","/walletservice.jar -Dspring.profiles.active=docker"] 
