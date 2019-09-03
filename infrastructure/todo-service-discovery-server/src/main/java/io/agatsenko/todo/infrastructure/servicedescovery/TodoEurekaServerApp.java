/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-25
 */
package io.agatsenko.todo.infrastructure.servicedescovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class TodoEurekaServerApp {
    public static void main(String[] args) {
        SpringApplication.run(TodoEurekaServerApp.class, args);
    }
}
