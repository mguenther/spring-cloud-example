FROM eclipse-temurin:17.0.7_7-jre-jammy

WORKDIR /opt/app

RUN adduser --system -shell /usr/sbin/nologin --group javauser

COPY target/gtd-config-server-1.0.0-SNAPSHOT.jar app.jar
RUN mkdir -p /config
ADD config config/

RUN chown -R javauser:javauser .

USER javauser

ENTRYPOINT ["java", "-jar", "app.jar"]