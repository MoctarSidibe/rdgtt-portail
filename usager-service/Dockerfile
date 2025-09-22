# Dockerfile pour Usager Service
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copier le pom.xml et télécharger les dépendances
COPY pom.xml .
COPY src ./src

# Installer Maven et construire l'application
RUN apt-get update && \
    apt-get install -y maven && \
    mvn clean package -DskipTests && \
    apt-get remove -y maven && \
    apt-get autoremove -y && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Copier le JAR construit
COPY target/*.jar app.jar

# Exposer le port
EXPOSE 8081

# Variables d'environnement par défaut
ENV SPRING_PROFILES_ACTIVE=production
ENV SERVER_PORT=8081

# Démarrer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]