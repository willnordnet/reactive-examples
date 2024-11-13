package servlet.examples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;

public class ServletApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ServletApplication.class);
        app.setWebApplicationType(WebApplicationType.SERVLET);
        app.run(args);
    }
}
