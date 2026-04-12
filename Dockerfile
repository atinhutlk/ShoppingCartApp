FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN apt-get update && apt-get install -y \
    libgtk-3-0 \
    libglib2.0-0 \
    libx11-6 \
    libxext6 \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libxxf86vm1 \
    libfontconfig1 \
    libfreetype6 \
    libasound2 \
    && rm -rf /var/lib/apt/lists/*

RUN mvn clean package

FROM maven:3.9.9-eclipse-temurin-21

LABEL authors="Nhut Vo"

WORKDIR /app

COPY --from=build /app /app

RUN apt-get update && apt-get install -y \
    libgtk-3-0 \
    libglib2.0-0 \
    libx11-6 \
    libxext6 \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libxxf86vm1 \
    libfontconfig1 \
    libfreetype6 \
    libasound2 \
    && rm -rf /var/lib/apt/lists/*

CMD ["mvn", "javafx:run"]