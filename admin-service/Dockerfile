# Multi-stage build pour optimiser la taille et la vitesse
FROM maven:3.8.6-openjdk-17-slim AS build

WORKDIR /app

# Copier seulement le pom.xml d'abord pour le cache des dépendances
COPY pom.xml .

# Télécharger les dépendances (cache si pom.xml n'a pas changé)
RUN mvn dependency:go-offline -B

# Copier le code source
COPY src ./src

# Construire l'application
RUN mvn clean package -DskipTests -B

# Stage de production
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copier le JAR depuis le stage de build
COPY --from=build /app/target/*.jar app.jar

# Exposer le port
EXPOSE 8085

# Variables d'environnement par défaut
ENV SPRING_PROFILES_ACTIVE=production
ENV SERVER_PORT=8085

# Démarrer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
