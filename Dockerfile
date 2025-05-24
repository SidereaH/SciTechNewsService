FROM gradle:8.4-jdk17 AS builder

WORKDIR /app

COPY build.gradle settings.gradle ./
RUN gradle --no-daemon dependencies

COPY . .

RUN gradle --no-daemon clean build -x test

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ENV SPRING_APPLICATION_NAME=SciTechNewsService
ENV SERVER_PORT=5084

EXPOSE 5084

ENTRYPOINT ["java", "-jar", "app.jar"]