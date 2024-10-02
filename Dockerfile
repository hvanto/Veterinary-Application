# Use a base image containing JDK
FROM openjdk:17-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the executable JAR file into the container
COPY .env /.env
COPY target/webapp*.jar /app.jar

# Specify the command to run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]

# Expose the port the application runs on
EXPOSE 8080
