package servlet.examples.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactive.examples.type.JuiceRequest;
import reactive.examples.type.JuiceResponse;

@RequestMapping("servlet")
@RestController
public class JuiceServletController {

    private static final Logger log = LoggerFactory.getLogger(JuiceServletController.class);

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

    private String prepareJuice(String cup, String appleJuice, String orangeJuice, String orderId) {
        log.info("Preparing juice with cup {}, appleJuice {}, orangeJuice {} and orderId {}", cup, appleJuice, orangeJuice, orderId);
        return "Final juice";
    }
}
