package servlet.examples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// The application should run with spring-boot-starter-web
@SpringBootApplication
public class ServletApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ServletApplication.class);
        app.setWebApplicationType(WebApplicationType.SERVLET);
        app.run(args);
    }
}
