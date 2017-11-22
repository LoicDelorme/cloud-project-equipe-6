# Initial image
FROM frolvlad/alpine-oraclejdk8:slim

# Working directories location for Tomcat
VOLUME /tmp

# Add JAR application to our container
ADD target/cloud-*.jar users_application.jar

# Launch our application using java -jar xxx.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/users_application.jar"]