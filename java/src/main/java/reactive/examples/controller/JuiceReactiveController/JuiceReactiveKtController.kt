package reactive.examples.controller.JuiceReactiveController;


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactive.examples.type.JuiceRequest
import reactive.examples.type.JuiceResponse

@RequestMapping("kt/reactive")
@RestController
class JuiceReactiveKtController {

    private val log = LoggerFactory.getLogger(JuiceReactiveKtController::class.java)

    @PostMapping("juice")
    fun orderJuice(@RequestBody request: JuiceRequest): Flow<JuiceResponse> = flow {
        val orderId = startOrder(request.username)
        val appleJuice = blendApple(orderId, request.apple)
        val orangeJuice = squeezeOrange(orderId, request.orange)
        val cup = findCup(appleJuice, orangeJuice)
        print(request.username, cup)
        val juice = prepareJuice(cup, appleJuice, orangeJuice, orderId)
        emit(JuiceResponse(juice))
    }

    @GetMapping("get")
    fun get(): Flow<String> {

        return flow {
            log("Hello")
            emit("Hello")
        }

    }

    private fun log(message: String) {
        log.info("Logging message {}", message)
    }

    private fun startOrder(username: String): String {
        log.info("Starting order for username {}", username)
        return "Some orderId"
    }

    private fun blendApple(orderId: String, apple: String): String {
        log.info("Blending apple {} for order {}", apple, orderId)
        return "Apple juice"
    }

    private fun squeezeOrange(orderId: String, orange: String): String {
        log.info("Squeezing orange {} for order {}", orange, orderId)
        return "Orange juice"
    }

    private fun findCup(appleJuice: String, orangeJuice: String): String {
        log.info("Finding a cup for appleJuice {} and orangeJuice {}", appleJuice, orangeJuice)
        return "Super big cup"
    }

    private fun print(username: String, cup: String) {
        log.info("Printing username {} on cup {}", username, cup)
    }

    private fun prepareJuice(cup: String, appleJuice: String, orangeJuice: String, orderId: String): String {
        log.info(
            "Preparing juice with cup {}, appleJuice {}, orangeJuice {} and orderId {}",
            cup,
            appleJuice,
            orangeJuice,
            orderId
        )
        return "Final juice"
    }
}
