    FROM adoptopenjdk:11-jre-hotspot
    RUN mkdir /opt/app
    COPY target/qieam-api-1.0-SNAPSHOT.jar /opt/app
    COPY target/dependency/* /opt/app/
    CMD ["java", "-cp", "/opt/app/*", "com.quartzinsight.qieam.Qieam"]

