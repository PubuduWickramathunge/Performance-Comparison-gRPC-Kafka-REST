FROM amazoncorretto:17.0.9

# Set environment variables
ENV SERVER_PORT=9053
ENV SPRING_APPLICATION_NAME=te02-order-service

# Set the working directory in the container
WORKDIR /app

ADD build/libs/te02-order-service-0.0.1-SNAPSHOT.jar te02-order-service.jar

# Expose the port that the app runs on
EXPOSE $SERVER_PORT

# Run the JAR file when the container launches
CMD ["java", "-jar", "/app/te02-order-service.jar"]
