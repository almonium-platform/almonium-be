# ---- Builder Stage ----
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# 1. Copy only pom.xml
COPY pom.xml .
# 2. Download dependencies (cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# 3. Copy your source code
COPY src ./src

# 4. Build the JAR (uses cached dependencies)
RUN mvn package -B -Dmaven.test.skip=true # This creates target/*.jar INSIDE this stage

# 5. Extract layers for the final image
# The ARG JAR_FILE might not be strictly necessary if your target/*.jar path is consistent
# from the mvn package command. You can directly use the path.
RUN java -Djarmode=layertools -jar target/*.jar extract

# ---- Runtime Stage ----
# Using eclipse-temurin:17-jre-jammy for the runtime stage is good for size
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder app/dependencies/ ./
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
