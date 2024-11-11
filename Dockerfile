FROM gradle:8.5-jdk21 AS build
WORKDIR /app

COPY build.gradle.kts settings.gradle.kts /app/

COPY gradle /app/gradle

COPY . /app

RUN gradle clean build -x test --no-daemon

FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar app.jar --spring.config.additional-location=file:///config/" ]