FROM amazoncorretto:21.0.9

WORKDIR /app

COPY target/cch-0.0.1-SNAPSHOT.jar /app/cch-0.0.1-SNAPSHOT.jar

# COPY target/spring-boot-security-postgresql-0.0.1-SNAPSHOT.jar /app/auth-service.jar
# COPY start.sh /app/start.sh




# RUN chmod +x /app/start.sh

# CMD ["/bin/bash", "/app/start.sh"]
CMD ["java", "-jar", "cch-0.0.1-SNAPSHOT.jar"]

