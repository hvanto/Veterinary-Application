# Use a base image containing JDK
FROM openjdk:17-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy Maven wrapper files and pom.xml to build the project inside the container
COPY .mvn/ .mvn
COPY mvnw .
COPY pom.xml .

# Copy the entire source code
COPY src/ src/

# Build the project using the Maven wrapper
RUN ./mvnw clean install -DskipTests

# Copy the executable JAR file into the container
COPY .env /.env

# Copy the built JAR file to the final image
COPY target/webapp*.jar /app.jar

# Specify the command to run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]

# Expose the port the application runs on
EXPOSE 8080
