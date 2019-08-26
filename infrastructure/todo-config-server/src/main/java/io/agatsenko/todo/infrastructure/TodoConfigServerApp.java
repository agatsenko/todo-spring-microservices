/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-25
 */
package io.agatsenko.todo.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class TodoConfigServerApp {
    public static void main(String[] args) {
        SpringApplication.run(TodoConfigServerApp.class, args);
    }
}
