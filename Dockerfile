FROM openjdk:8
ADD target/students-mysql.jar students-mysql.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "students-mysql.jar"]