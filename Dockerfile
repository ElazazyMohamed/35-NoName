FROM openjdk:25-ea-4-jdk-oraclelinux9

WORKDIR /app

COPY target/mini1.jar /app/app.jar

VOLUME /data

COPY ./src/main/java/com/example/data /data/

EXPOSE 8080

ENV SPRING_DATA_USER=/data/user.json
ENV SPRING_DATA_PRODUCT=/data/product.json
ENV SPRING_DATA_CART=/data/cart.json
ENV SPRING_DATA_ORDERS=/data/orders.json

CMD ["java","-jar","/app/app.jar"]