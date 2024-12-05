package reactive.examples.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import type.JuiceRequest;
import type.JuiceResponse;

import java.time.Duration;

@RequestMapping("reactive")
@RestController
public class JuiceReactiveController {


    private static final Logger log = LoggerFactory.getLogger(JuiceReactiveController.class);

    // Reactive and declarative
    @PostMapping("juice")
    public Mono<JuiceResponse> juice(@RequestBody JuiceRequest request) {
        return blendApple(request.apple())
                .zipWith(squeezeOrange(request.orange()))
                .flatMap(tuple -> prepareJuice(tuple.getT1(), tuple.getT2()))
                .map(JuiceResponse::new);
    }

    @PostMapping("blockJuice")
    public JuiceResponse blockJuice(@RequestBody JuiceRequest request) {
        return juice(request).block();
    }

    @PostMapping("sleepJuice")
    public JuiceResponse sleepJuice() throws InterruptedException {
        Thread.sleep(1000);
        log.info("Preparing sleep juice with Apple juice and Orange juice");
        return new JuiceResponse("Juice with Apple juice and Orange juice");
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

    // Batch or Stream
    @PostMapping(path = "/flux/juice", produces = {"application/stream+json", "application/json"})
    public Flux<JuiceResponse> fluxJuice(@RequestBody JuiceRequest request) {
        return blendAppleFlux(request.apple())
                .zipWith(squeezeOrangeFlux(request.orange()))
                .flatMap(tuple -> prepareJuice(tuple.getT1(), tuple.getT2()))
                .map(JuiceResponse::new);
    }

    // IO heavy
    private Mono<String> blendApple(int apple) {
        log.info("Blending {} apples", apple);
        return Mono.delay(Duration.ofSeconds(1)).then(Mono.just("Apple juice"));
    }

    // IO heavy
    private Mono<String> squeezeOrange(int orange) {
        log.info("Squeezing {} orances", orange);
        return Mono.delay(Duration.ofSeconds(2)).then(Mono.just("Orange juice"));
    }

    // Blend 5 apples in 5s
    private Flux<String> blendAppleFlux(int numOfApples) {
        log.info("Blending {} apples", numOfApples);
        return Flux.interval(Duration.ofSeconds(1))
                .map(i -> "apple " + i)
                .doOnNext(s -> log.info("Blending {}", s))
                .take(numOfApples);
    }

    // Squeeze 5 oranges in 10s
    private Flux<String> squeezeOrangeFlux(int numOfOranges) {
        log.info("Squeezing {} oranges", numOfOranges);
        return Flux.interval(Duration.ofSeconds(2))
                .map(i -> "orange " + i)
                .doOnNext(s -> log.info("Squeezing {}", s))
                .take(numOfOranges);
    }

    private Mono<String> prepareJuice(String appleJuice, String orangeJuice) {
        log.info("Preparing juice with {} and {}", appleJuice, orangeJuice);
        return Mono.just("Juice with %s and %s".formatted(appleJuice, orangeJuice));
    }

}
