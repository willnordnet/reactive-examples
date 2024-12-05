package controller;


import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("kt/reactive")
@RestController
class JuiceReactiveKtController {

    private val log = LoggerFactory.getLogger(JuiceReactiveKtController::class.java)

    @GetMapping("block/juice")
    suspend fun blockJuice(): String {
        mixJuiceSuspend()
        log.info("Prepared final juice")
        return "Final juice"
    }

    @GetMapping("flow/juice")
    fun flowJuice(): Flow<String> = flow {
        log.info("Starting flow order")
        delay(1000) // Sleep for 1000 milliseconds
        emit("Final juice")
    }

    private fun mixJuiceBlock() = runBlocking {
        squeezeOrange().zip(blendApple()) { orange, apple -> "$orange + $apple" }
            .collect { log.info("Mixed juice: $it") }
    }

    private suspend fun mixJuiceSuspend() =
        squeezeOrange().zip(blendApple()) { orange, apple -> "$orange + $apple" }
            .collect { log.info("Mixed juice: $it") }


    private fun squeezeOrange(): Flow<String> = flow {
        log.info("Squeezing orange")
        delay(1000)
        emit("Orange juice")
    }

    private fun blendApple(): Flow<String> = flow {
        log.info("Blending apple")
        delay(2000)
        emit("Apple juice")
    }
}
