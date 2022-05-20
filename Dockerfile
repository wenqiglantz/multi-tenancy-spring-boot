FROM amazoncorretto:17-alpine-jdk
MAINTAINER github.com/wenqiglantz
RUN mkdir -m777 /etc/customer-service
ADD start-service.sh /etc/customer-service
ADD target/customerservice-0.0.1-SNAPSHOT-exec.jar /etc/customer-service
COPY postgre.jks /tmp/postgre.jks
RUN chmod +x /etc/customer-service/start-service.sh
WORKDIR /etc/customer-service
EXPOSE 8500
CMD sh start-service.sh
