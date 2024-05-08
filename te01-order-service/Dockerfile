FROM amazoncorretto:17.0.9

# environment variables
ENV SERVER_PORT=9050
ENV SPRING_APPLICATION_NAME=te01-order-service
ENV RESEARCH_TE01_INVENTORY_SERVICE_GRPC_CLIENT_HOST=localhost
ENV RESEARCH_TE01_INVENTORY_SERVICE_GRPC_CLIENT_PORT=9051
ENV RESEARCH_TE01_PAYMENT_SERVICE_GRPC_CLIENT_HOST=localhost
ENV RESEARCH_TE01_PAYMENT_SERVICE_GRPC_CLIENT_PORT=9052

# the working directory in the container
WORKDIR /app

ADD build/libs/te01-order-service-0.0.1-SNAPSHOT.jar te01-order-service.jar

# the port that the app runs on
EXPOSE $SERVER_PORT

# Run the JAR file when the container launches
CMD ["java", "-jar", "/app/te01-order-service.jar"]
