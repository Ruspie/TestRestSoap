package org.example.testrestsoap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class TestRestSoapApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(TestRestSoapApplication.class, args);
    }

}
