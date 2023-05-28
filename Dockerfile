FROM azul/zulu-openjdk-alpine:17.0.4.1

WORKDIR /app

COPY target/MyNotes-0.0.1-SNAPSHOT.jar app.jar

ENV PORT=8080

CMD ["java", "-jar", "app.jar"]
