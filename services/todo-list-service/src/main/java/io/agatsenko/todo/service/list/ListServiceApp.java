/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-21
 */
package io.agatsenko.todo.service.list;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@EnableOAuth2Client
@EnableResourceServer
@EnableEurekaClient
@EnableCircuitBreaker
@EnableFeignClients(basePackages = {
        "io.agatsenko.todo.service.auth.client"
})
@SpringBootApplication
public class ListServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(ListServiceApp.class, args);
    }
}
