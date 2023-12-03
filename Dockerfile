FROM maven:3.9.5-amazoncorretto-8-al2023 as BUILD


COPY client/src /home/client/src
COPY client/pom.xml /home/client
COPY pom.xml /home

WORKDIR /home
RUN mvn -f /home/pom.xml clean install

FROM amazoncorretto:8
COPY --from=BUILD /home/client/target/client-1.0-SNAPSHOT.jar /home/client.jar
WORKDIR /home
CMD ["java", "-jar", "client.jar"]

