package reactive.examples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// The application should run without spring-boot-starter-web
@SpringBootApplication
public class ReactiveApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ReactiveApplication.class);
        app.setWebApplicationType(WebApplicationType.REACTIVE);
        app.run(args);
    }
}
