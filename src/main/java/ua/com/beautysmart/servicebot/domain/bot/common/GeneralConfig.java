package ua.com.beautysmart.servicebot.domain.bot.common;

import com.pnuema.java.barcode.Barcode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

@Configuration
public class GeneralConfig {

    @Value(value = "${bot-token}")
    private String botToken;

    @Value(value = "${bot-username}")
    private String botUsername;


    @Bean
    public HttpClient httpClient() {
        return HttpClient.newHttpClient();
    }

    @Bean
    public String botToken() {
        return botToken;
    }

    @Bean
    public String botUsername() {
        return botUsername;
    }

    @Bean
    public Barcode barcode(){
        return new Barcode();
    }

}
