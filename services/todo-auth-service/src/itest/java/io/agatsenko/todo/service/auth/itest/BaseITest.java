/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-11-03
 */
package io.agatsenko.todo.service.auth.itest;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseITest {
    private static final String BASE_URI = "http://localhost";
    private static final String BASE_PATH = "/uaa";

    @LocalServerPort
    protected int serverPort;

    @BeforeEach
    void setupRestAssured() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = serverPort;
        RestAssured.basePath = BASE_PATH;
    }
}
