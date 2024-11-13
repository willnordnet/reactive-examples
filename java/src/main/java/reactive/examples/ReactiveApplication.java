package reactive.examples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReactiveApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ReactiveApplication.class);
        app.setWebApplicationType(WebApplicationType.REACTIVE);
        app.run(args);
    }
}
