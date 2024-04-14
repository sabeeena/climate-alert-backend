FROM openjdk:11-jre

ADD ./target/data-service-0.0.1-SNAPSHOT.jar /app/app.jar

CMD ["java", "-Xmx500m", "-jar", "/app/app.jar"]

EXPOSE 8084
