FROM openjdk:11-jre

ADD ./target/auth-gateway.jar /app/auth-gateway.jar

CMD ["java", "-Xmx200m", "-jar", "/app/auth-gateway.jar"]

EXPOSE 8011
