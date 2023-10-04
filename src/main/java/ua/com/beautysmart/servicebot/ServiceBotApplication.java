package ua.com.beautysmart.servicebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServiceBotApplication {

    public static void main(String[] args) {
        System.out.println(System.getenv().get("JDBC_URL"));
        SpringApplication.run(ServiceBotApplication.class, args);
    }

}
