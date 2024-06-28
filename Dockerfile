FROM gradle:7.6.0-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/project
WORKDIR /home/gradle/project
RUN gradle build --no-daemon -x test

FROM openjdk:17-jdk-slim
COPY --from=build /home/gradle/project/build/libs/*.jar /usr/app/myapp.jar
ENTRYPOINT ["java", "-jar", "/usr/app/myapp.jar"]