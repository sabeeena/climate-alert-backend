FROM openjdk:11-jre

ADD ./target/report-sevice-0.0.1-SNAPSHOT.jar /app/auth-gateway.jar

CMD ["java", "-Xmx200m", "-jar", "/app/auth-gateway.jar"]

EXPOSE 8086
