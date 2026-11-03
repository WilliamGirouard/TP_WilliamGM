FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S william && adduser -S william -G william
WORKDIR /app
COPY target/*.jar app.jar
USER william
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]