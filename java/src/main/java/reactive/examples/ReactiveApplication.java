package reactive.examples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import reactor.netty.resources.LoopResources;

import java.lang.reflect.Field;

// The application should run without spring-boot-starter-web
@SpringBootApplication
public class ReactiveApplication {

    public ReactiveApplication(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ReactiveApplication.class);
        app.setWebApplicationType(WebApplicationType.REACTIVE);
        app.run(args);
    }

    final ApplicationContext applicationContext;

    @Bean
    public LoopResources loopResources() {
        return LoopResources.create("webflux-io", 1, 4, true);
    }


    @EventListener
    public void onApplicationReady(ApplicationReadyEvent event) throws NoSuchFieldException, IllegalAccessException {
        boolean isReactive = !applicationContext.getBeansOfType(ReactiveWebServerFactory.class).isEmpty();

        if (isReactive) {
            System.out.println("The application is using WebFlux pool. ===============");
        } else {
            System.out.println("The application is using Tomcat thread pool. ===============");
        }


        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("========== Number of CPU cores: " + cores);

        LoopResources loopResources = loopResources();
        Field workerCountField = loopResources.getClass().getDeclaredField("workerCount");
        workerCountField.setAccessible(true);
        int workerCount = (int) workerCountField.get(loopResources);
        System.out.println("========== Number of WebFlux worker threads: " + workerCount);
    }
}

