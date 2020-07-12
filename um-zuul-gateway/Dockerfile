FROM openjdk:11-jdk
COPY um-zuul-gateway.jar um-zuul-gateway.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","um-zuul-gateway.jar"]
