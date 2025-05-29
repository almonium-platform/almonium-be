# ---- Builder Stage ----
FROM openjdk:17-jdk-slim AS builder
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
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# ---- Runtime Stage ----
FROM openjdk:17-jre-slim
WORKDIR /app
COPY --from=builder app/dependencies/ ./
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
