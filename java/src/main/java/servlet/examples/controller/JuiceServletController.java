package servlet.examples.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import type.JuiceRequest;
import type.JuiceResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

@RequestMapping("servlet")
@RestController
public class JuiceServletController {

    private static final Logger log = LoggerFactory.getLogger(JuiceServletController.class);

    @PostMapping("blend")
    public JuiceResponse blend(@RequestBody JuiceRequest request) {
        var appleJuice = blendApple(request.apple());
        var orangeJuice = squeezeOrange(request.orange());
        var juice = prepareJuice(appleJuice, orangeJuice);
        return new JuiceResponse(juice);
    }

    @PostMapping("blendCF")
    public JuiceResponse blendCF(@RequestBody JuiceRequest request) {
        CompletableFuture<String> appleJuice = CompletableFuture.supplyAsync(() -> blendApple(request.apple()));
        CompletableFuture<String> orangeJuice = CompletableFuture.supplyAsync(() -> squeezeOrange(request.orange()));
        var juice = prepareJuice(appleJuice.join(), orangeJuice.join());
        return new JuiceResponse(juice);
    }

    @PostMapping("blendVT")
    public JuiceResponse blendVT(@RequestBody JuiceRequest request) throws InterruptedException {
        AtomicReference<String> appleJuice = new AtomicReference<>();
        final Thread vt1 = Thread.ofVirtual().start(() -> appleJuice.set(blendApple(request.apple())));

        AtomicReference<String> orangeJuice = new AtomicReference<>();
        final Thread vt2 = Thread.ofVirtual().start(() -> orangeJuice.set(squeezeOrange(request.orange())));

        vt1.join();
        vt2.join();

        var juice = prepareJuice(appleJuice.get(), orangeJuice.get());
        return new JuiceResponse(juice);
    }

    @PostMapping("juice")
    public JuiceResponse orderJuice(@RequestBody JuiceRequest request) {
        var orderId = startOrder(request.username());
        var appleJuice = blendApple(orderId, request.apple());
        var orangeJuice = squeezeOrange(orderId, request.orange());
        var cup = findCup(appleJuice, orangeJuice);
        print(request.username(), cup);
        var juice = prepareJuice(cup, appleJuice, orangeJuice, orderId);
        return new JuiceResponse(juice);
    }

    private String startOrder(String username) {
        log.info("Starting order for username {}", username);
        return "Some orderId";
    }

    private String blendApple(String apple) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("Blending apple {}", apple);
        return "Apple juice";
    }

    private String squeezeOrange(String orange) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("Squeezing orange {}", orange);
        return "Orange juice";
    }

    private String blendApple(String orderId, String apple) {
        log.info("Blending apple {} for order {}", apple, orderId);
        return "Apple juice";
    }

    private String squeezeOrange(String orderId, String orange) {
        log.info("Squeezing orange {} for order {}", orange, orderId);
        return "Orange juice";
    }

    private String findCup(String appleJuice, String orangeJuice) {
        log.info("Finding a cup for appleJuice {} and orangeJuice {}", appleJuice, orangeJuice);
        return "Super big cup";
    }

    private void print(String username, String cup) {
        log.info("Printing username {} on cup {}", username, cup);
    }

    private String prepareJuice(String appleJuice, String orangeJuice) {
        log.info("Preparing juice with appleJuice {}, orangeJuice {}", appleJuice, orangeJuice);
        return "Final juice";
    }

    private String prepareJuice(String cup, String appleJuice, String orangeJuice, String orderId) {
        log.info("Preparing juice with cup {}, appleJuice {}, orangeJuice {} and orderId {}", cup, appleJuice, orangeJuice, orderId);
        return "Final juice";
    }
}
