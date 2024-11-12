package reactive.examples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;

public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebApplicationType(WebApplicationType.REACTIVE);
        app.run(args);
    }
}
