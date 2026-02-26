package web.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = "web")
@SpringBootApplication
@EntityScan("web.model")
@EnableJpaRepositories("web.repository")
public class Lesson9Application {

    public static void main(String[] args) {
        SpringApplication.run(Lesson9Application.class, args);
    }

}
