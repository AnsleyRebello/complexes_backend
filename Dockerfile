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
COPY --from=builder /workspace/target/*.jar app.jar
# Let Render dynamically assign the port
ENV PORT=10000
EXPOSE 10000
ENTRYPOINT ["sh", "-c", "java -Dserver.port=$PORT -jar /app/app.jar"]

#ansley
