FROM maven:3.5-jdk-8
WORKDIR /opt/data-extractor
COPY pom.xml .
RUN mvn -q dependency:go-offline
COPY src src
