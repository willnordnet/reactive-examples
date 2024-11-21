package servlet.examples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;

// The application should run with spring-boot-starter-web
@SpringBootApplication
public class ServletApplication {

    private final ApplicationContext applicationContext;

    public ServletApplication(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ServletApplication.class);
        app.setWebApplicationType(WebApplicationType.SERVLET);
        app.run(args);
    }

    @EventListener
    public void onApplicationReady(ApplicationReadyEvent event) {
        boolean isReactive = !applicationContext.getBeansOfType(ReactiveWebServerFactory.class).isEmpty();

        if (isReactive) {
            System.out.println("The application is using WebFlux pool. ===============");
        } else {
            System.out.println("The application is using Tomcat thread pool. ===============");
        }
    }
}
