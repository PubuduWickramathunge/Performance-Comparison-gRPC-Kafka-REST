FROM amazoncorretto:17.0.9

# Set the working directory in the container
WORKDIR /app

ADD build/libs/te03-payment-service-0.0.1-SNAPSHOT.jar te03-payment-service.jar

# Set environment variables from Dockerfile
ENV SERVER_PORT 9055
ENV SPRING_APPLICATION_NAME te03-payment-service

# Expose server port
EXPOSE $SERVER_PORT

# Start the application using the Spring Boot main class
CMD ["java", "-jar", "/app/te03-payment-service.jar"]
