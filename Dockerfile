# ---- Build stage ----
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /workspace
COPY mvnw ./
COPY .mvn .mvn
COPY pom.xml ./
COPY src ./src
RUN mvn -B -DskipTests package

# ---- Runtime stage ----
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the jar built in previous stage
COPY --from=builder /workspace/target/*.jar app.jar

# ---- Firebase Setup ----
# Render env will contain: GCP_SA_JSON
# Create firebase.json file from env variable and set credentials path before starting
ENV GOOGLE_APPLICATION_CREDENTIALS=/tmp/firebase.json
ENV PORT=10000
EXPOSE 10000

ENTRYPOINT ["/bin/sh", "-c", "echo \"$GCP_SA_JSON\" > /tmp/firebase.json && java -Dserver.port=$PORT -jar /app/app.jar"]
