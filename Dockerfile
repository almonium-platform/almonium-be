FROM eclipse-temurin:21-jre-alpine AS extractor
WORKDIR /app

COPY target/*.jar app.jar
RUN java -Djarmode=tools -jar app.jar extract --layers --destination extracted

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY --from=extractor /app/extracted/dependencies/ ./
COPY --from=extractor /app/extracted/spring-boot-loader/ ./
COPY --from=extractor /app/extracted/snapshot-dependencies/ ./
COPY --from=extractor /app/extracted/application/ ./

ENTRYPOINT ["java", "-Djava.net.preferIPv4Stack=true", "org.springframework.boot.loader.launch.JarLauncher"]
