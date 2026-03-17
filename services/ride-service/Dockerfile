# Image Java
FROM eclipse-temurin:17-jdk-alpine

# dossier de travail
WORKDIR /app

# copier le jar
COPY target/*.jar app.jar

# port du service
EXPOSE 8082

# démarrer l'application
ENTRYPOINT ["java","-jar","app.jar"]