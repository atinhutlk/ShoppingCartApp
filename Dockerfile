FROM maven:3.9.9-eclipse-temurin-21

LABEL authors="Nhut Vo"

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package

CMD ["mvn", "test"]