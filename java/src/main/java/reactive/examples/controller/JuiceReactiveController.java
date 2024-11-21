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


    @PostMapping("juice")
    public Mono<JuiceResponse> juice(@RequestBody JuiceRequest request) {
        return blendApple(request.apple())
                .zipWith(squeezeOrange(request.orange()))
                .flatMap(tuple -> prepareJuice(tuple.getT1(), tuple.getT2()))
                .map(JuiceResponse::new);
    }

    @PostMapping(path = "/flux/juice", produces = {"application/stream+json", "application/json"})
    public Flux<JuiceResponse> fluxJuice(@RequestBody JuiceRequest request) {
        return blendAppleFlux(request.apple())
                .zipWith(squeezeOrangeFlux(request.orange()))
                .flatMap(tuple -> prepareJuice(tuple.getT1(), tuple.getT2()))
                .map(JuiceResponse::new);
    }

    @PostMapping("prime")
    public Mono<Boolean> prime(@RequestBody long number) {
        log.info("Checking if {} is a prime number", number);
        for (long i = 2; i <= number; i++) {
            if (number % i == 0) {
                log.info("{} is a prime number", number);
                return Mono.just(true);
            }
        }
        return Mono.just(false);
    }


    @PostMapping("juice2")
    public Mono<JuiceResponse> orderJuice(@RequestBody JuiceRequest request) {
        return startOrder(request.username())
                .flatMap(orderId -> blendApple(orderId, request.apple())
                        .zipWith(squeezeOrange(orderId, request.orange()))
                        .flatMap(tuple -> findCup(tuple.getT1(), tuple.getT2())
                                .flatMap(cup -> print(request.username(), cup)
                                        .then(prepareJuice(cup, tuple.getT1(), tuple.getT2(), orderId))
                                        .map(JuiceResponse::new))));
    }

    @PostMapping("juiceWithIntermediateDTO")
    public Mono<JuiceResponse> orderJuice2(@RequestBody JuiceRequest request) {
        return startOrder(request.username())
                .map(orderId -> new IntermediateDTO(orderId, null, request.apple(), request.orange(), null, null))
                .flatMap(intermediateDTO -> blendApple(intermediateDTO.orderId(), intermediateDTO.appleJuice())
                        .map(appleResult -> new IntermediateDTO(intermediateDTO.orderId(), null, intermediateDTO.appleJuice(), intermediateDTO.orangeJuice(), appleResult, null)))
                .flatMap(intermediateDTO2 -> squeezeOrange(intermediateDTO2.orderId, intermediateDTO2.orange())
                        .map(orangeResult -> new IntermediateDTO(intermediateDTO2.orderId, null, intermediateDTO2.apple(), intermediateDTO2.orange(), intermediateDTO2.appleJuice, orangeResult)))
                .flatMap(intermediateDTO3 -> findCup(intermediateDTO3.appleJuice, intermediateDTO3.orangeJuice)
                        .map(cup -> new IntermediateDTO(intermediateDTO3.orderId, cup, intermediateDTO3.apple(), intermediateDTO3.orange(), intermediateDTO3.appleJuice, intermediateDTO3.orangeJuice)))
                .flatMap(intermediateDTO4 -> print(request.username(), intermediateDTO4.cup())
                        .then(Mono.fromCallable(() -> new IntermediateDTO(intermediateDTO4.orderId, intermediateDTO4.cup, intermediateDTO4.apple(), intermediateDTO4.orange(), intermediateDTO4.appleJuice, intermediateDTO4.orangeJuice))))
                .flatMap(intermediateDTO5 -> prepareJuice(intermediateDTO5.cup, intermediateDTO5.appleJuice, intermediateDTO5.orangeJuice, intermediateDTO5.orderId)
                        .map(JuiceResponse::new));
    }


    private Mono<String> startOrder(String username) {
        log.info("Starting order for username {}", username);
        return Mono.just("Some orderId");
    }

    private Mono<String> blendApple(String orderId, String apple) {
        log.info("Blending apple {} for order {}", apple, orderId);
        return Mono.just("Apple juice");
    }

    private Mono<String> squeezeOrange(String orderId, String orange) {
        log.info("Squeezing orance {} for order {}", orange, orderId);
        return Mono.just("Orange juice");
    }

    private Mono<String> blendApple(String apple) {
        log.info("Blending apple {}", apple);
        return Mono.delay(Duration.ofSeconds(1)).then(Mono.just("Apple juice"));
    }


    private Mono<String> squeezeOrange(String orange) {
        log.info("Squeezing orance {}", orange);
        return Mono.delay(Duration.ofSeconds(2)).then(Mono.just("Orange juice"));
    }

    private Flux<String> blendAppleFlux(String apple) {
        log.info("Blending apple flux {}", apple);
        return Flux.interval(Duration.ofSeconds(1))
                .map(i -> "apple " + i)
                .doOnNext(s -> log.info("Blending {}", s))
                .take(5);
    }

    private Flux<String> squeezeOrangeFlux(String orange) {
        log.info("Squeezing orange flux {}", orange);
        return Flux.interval(Duration.ofSeconds(2))
                .map(i -> "orange " + i)
                .doOnNext(s -> log.info("Squeezing {}", s))
                .take(5);
    }

    private Mono<String> findCup(String appleJuice, String orangeJuice) {
        log.info("Finding a cup for appleJuice {} and orangeJuice {}", appleJuice, orangeJuice);
        return Mono.just("Super big cup");
    }

    private Mono<Void> print(String username, String cup) {
        log.info("Printing username {} on cup {}", username, cup);
        return Mono.empty();
    }

    private Mono<String> prepareJuice(String appleJuice, String orangeJuice) {
        log.info("Preparing juice with {} and {}", appleJuice, orangeJuice);
        return Mono.just("Juice with %s and %s".formatted(appleJuice, orangeJuice));
    }

    private Mono<String> prepareJuice(String cup, String appleJuice, String orangeJuice, String orderId) {
        log.info("Preparing juice with cup {}, appleJuice {}, orangeJuice {} and orderId {}", cup, appleJuice, orangeJuice, orderId);
        return Mono.just("Final juice");
    }

    private record IntermediateDTO(String orderId, String cup, String apple, String orange, String appleJuice,
                                   String orangeJuice) {

    }


}
