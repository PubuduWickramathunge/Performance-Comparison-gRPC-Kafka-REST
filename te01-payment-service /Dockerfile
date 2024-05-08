FROM amazoncorretto:17.0.4

# Set the working directory in the container
WORKDIR /app

ADD build/libs/te01-payment-service-0.0.1-SNAPSHOT.jar te01-payment-service.jar

# Set environment variables from Dockerfile
ENV GRPC_PORT 9052
ENV SPRING_APPLICATION_NAME te01-payment-service

# Expose server port
EXPOSE $GRPC_PORT

# Start the application using the Spring Boot main class
CMD ["java", "-jar", "/app/te01-payment-service.jar"]
