# Use a slightly more robust base image
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Explicitly copy the jar and give it full permissions
COPY target/planner-0.0.1-SNAPSHOT.jar /app/app.jar
RUN chmod +x /app/app.jar

# Force UTF-8 encoding to prevent the IBM437 corruption
ENV JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF-8"

ENTRYPOINT ["java", "-jar", "/app/app.jar"]