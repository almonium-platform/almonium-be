# ---- Builder Stage (Only for layertools extraction) ----
FROM openjdk:17-jdk-slim AS layertools-extractor
WORKDIR /app
COPY target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# ---- Runtime Stage ----
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

COPY --from=layertools-extractor /app/dependencies/ ./
COPY --from=layertools-extractor /app/spring-boot-loader/ ./
COPY --from=layertools-extractor /app/snapshot-dependencies/ ./
COPY --from=layertools-extractor /app/application/ ./

ENTRYPOINT ["java", "-Djava.net.preferIPv4Stack=true", "org.springframework.boot.loader.launch.JarLauncher"]
