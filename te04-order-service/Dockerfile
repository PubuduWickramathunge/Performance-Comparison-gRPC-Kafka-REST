FROM amazoncorretto:17.0.9

# Set environment variables
ENV SERVER_PORT=9059
ENV SPRING_APPLICATION_NAME=te04-order-service
ENV RESEARCH_TE01_INVENTORY_SERVICE_GRPC_CLIENT_HOST=localhost
ENV RESEARCH_TE01_INVENTORY_SERVICE_GRPC_CLIENT_PORT=9051
ENV RESEARCH_TE01_PAYMENT_SERVICE_GRPC_CLIENT_HOST=localhost
ENV RESEARCH_TE01_PAYMENT_SERVICE_GRPC_CLIENT_PORT=9052

# Set the working directory in the container
WORKDIR /app

ADD build/libs/te04-order-service-0.0.1-SNAPSHOT.jar te04-order-service.jar

# Expose the port that the app runs on
EXPOSE $SERVER_PORT

# Run the JAR file when the container launches
CMD ["java", "-jar", "/app/te04-order-service.jar"]
