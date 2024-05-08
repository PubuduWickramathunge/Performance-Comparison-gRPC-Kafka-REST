FROM amazoncorretto:17.0.9

# Set the working directory in the container
WORKDIR /app

ADD build/libs/te02-payment-service-0.0.1-SNAPSHOT.jar te02-payment-service.jar

# Set environment variables from Dockerfile
ENV SERVER_PORT 9055
ENV SPRING_APPLICATION_NAME te02-payment-service

# Expose server port
EXPOSE $SERVER_PORT

# Start the application using the Spring Boot main class
CMD ["java", "-jar", "/app/te02-payment-service.jar"]
