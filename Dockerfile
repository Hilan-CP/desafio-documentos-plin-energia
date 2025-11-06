FROM maven AS build
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn install -DskipTests

FROM eclipse-temurin:21
WORKDIR /app
COPY --from=build /build/target/documents-0.0.1-SNAPSHOT.jar /app
EXPOSE 8080
CMD ["java","-jar","documents-0.0.1-SNAPSHOT.jar"]
