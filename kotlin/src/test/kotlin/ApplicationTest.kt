import app.Application
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.Duration
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [Application::class])
class ApplicationTest {

    private lateinit var webTestClient: WebTestClient

    @LocalServerPort
    private var port: Int = 0

    private var startTime: Long = 0

    @BeforeEach
    fun setUp() {
        webTestClient = WebTestClient
            .bindToServer()
            .baseUrl("http://localhost:$port")
            .responseTimeout(Duration.ofSeconds(15))
            .build()
        startTime = System.currentTimeMillis()
    }

    @AfterEach
    fun afterEach() {
        println("======== Total time used: ${System.currentTimeMillis() - startTime} ms")
    }

    @Test
    fun getFlowJuiceKt() {
        val responseBody = webTestClient.get()
            .uri("kt/reactive/flow/juice")
            .exchange()
            .expectStatus().isOk
            .returnResult(String::class.java)
            .responseBody
            .collectList()
            .block()

        assertEquals(listOf("Final juice"), responseBody)
    }
}
