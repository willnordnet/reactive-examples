package controller;


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("kt/reactive")
@RestController
class JuiceReactiveKtController {

    private val log = LoggerFactory.getLogger(JuiceReactiveKtController::class.java)

    @GetMapping("flow/juice")
    fun flowJuice(): Flow<String> =
        flow {
            log.info("Starting flow order")
            emit("Final juice")
        }
}
