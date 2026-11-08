
# =====================================
# STAGE 1: BUILD APPLICATION
# =====================================

FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .

COPY src ./src

RUN mvn clean package -DskipTests

# =====================================
# STAGE 2: RUN APPLICATION
# =====================================

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/quarkus-app/ ./quarkus-app/

EXPOSE 8080

ENV QUARKUS_HTTP_HOST=0.0.0.0

CMD ["java", "-jar", "quarkus-app/quarkus-run.jar"]
