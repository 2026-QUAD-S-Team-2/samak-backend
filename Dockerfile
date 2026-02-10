FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY build/libs/samak-app.jar /app/samak-app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/app/samak-app.jar"]
