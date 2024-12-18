package servlet.examples.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import type.JuiceRequest;
import type.JuiceResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

@RequestMapping("servlet")
@RestController
public class JuiceServletController {

    private static final Logger log = LoggerFactory.getLogger(JuiceServletController.class);

    // Sequential and imperative
    @PostMapping("juice")
    public JuiceResponse juice(@RequestBody JuiceRequest request) {
        var appleJuice = blendApple(request.apple());
        var orangeJuice = squeezeOrange(request.orange());
        var juice = prepareJuice(appleJuice, orangeJuice);
        return new JuiceResponse(juice);
    }

    // Concurrent and imperative
    @PostMapping("cf/juice")
    public JuiceResponse juiceCF(@RequestBody JuiceRequest request) {
        CompletableFuture<String> appleJuice = CompletableFuture.supplyAsync(() -> blendApple(request.apple()));
        CompletableFuture<String> orangeJuice = CompletableFuture.supplyAsync(() -> squeezeOrange(request.orange()));

        var juice = prepareJuice(appleJuice.join(), orangeJuice.join());
        return new JuiceResponse(juice);
    }

    // Concurrent and imperative
    @PostMapping("vt/juice")
    public JuiceResponse juiceVT(@RequestBody JuiceRequest request) throws InterruptedException {
        AtomicReference<String> appleJuice = new AtomicReference<>();
        final Thread vt1 = Thread.ofVirtual().start(() -> appleJuice.set(blendApple(request.apple())));

        AtomicReference<String> orangeJuice = new AtomicReference<>();
        final Thread vt2 = Thread.ofVirtual().start(() -> orangeJuice.set(squeezeOrange(request.orange())));

        vt1.join();
        vt2.join();

        var juice = prepareJuice(appleJuice.get(), orangeJuice.get());
        return new JuiceResponse(juice);
    }

    // Computation heavy
    @PostMapping("primeJuice")
    public Mono<Boolean> primeJuice(@RequestBody long number) {
        log.info("Checking if {} is a prime number", number);
        for (long i = 2; i <= number; i++) {
            if (number % i == 0) {
                log.info("{} is a prime number", number);
                return Mono.just(true);
            }
        }
        return Mono.just(false);
    }

    private String blendApple(int apple) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("Blending {} apple", apple);
        return "Apple juice";
    }

    private String squeezeOrange(int orange) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("Squeezing {} orange", orange);
        return "Orange juice";
    }

    private String prepareJuice(String appleJuice, String orangeJuice) {
        log.info("Preparing juice with {}, {}", appleJuice, orangeJuice);
        return "Final juice";
    }
}
