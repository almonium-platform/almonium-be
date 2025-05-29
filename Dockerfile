FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

ARG EXTRACTED_LAYERS_DIR=target/extracted

COPY ${EXTRACTED_LAYERS_DIR}/dependencies/ ./
COPY ${EXTRACTED_LAYERS_DIR}/spring-boot-loader/ ./
COPY ${EXTRACTED_LAYERS_DIR}/snapshot-dependencies/ ./
COPY ${EXTRACTED_LAYERS_DIR}/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
