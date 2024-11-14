package reactive.examples;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactive.examples.type.JuiceRequest;

import java.util.stream.IntStream;

// Remove spring-boot-starter-web
@SpringBootTest(classes = ReactiveApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReactiveApplicationTest {

    private WebTestClient webTestClient;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
    }

    @Test
    void orderJuice() {
        webTestClient.post()
                .uri("reactive/juice")
                .bodyValue(new JuiceRequest("username-1", "apple-1", "orange-1"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("""
                        {
                            "juice": "Final juice"
                        }""");
    }

    @Test
    void orderJuiceKt() {
        webTestClient.post()
                .uri("kt/reactive/juice")
                .bodyValue(new JuiceRequest("username-1", "apple-1", "orange-1"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("""
                        {
                            "juice": "Final juice"
                        }""");
    }

    @Test
    void order10Juice() throws InterruptedException {
        IntStream.range(0, 10).forEach(i -> {
            String username = "username-" + i;
            Thread.ofVirtual().start(
                    () -> webTestClient.post()
                            .uri("reactive/juice")
                            .bodyValue(new JuiceRequest(username, "apple", "orange"))
                            .exchange()
                            .expectStatus().isOk()
                            .expectBody()
                            .json("""
                                    {
                                        "juice": "Final juice"
                                    }"""));
        });

        Thread.sleep(3000);
    }

    @Test
    void orderJuiceWithIntermediateDTO() {
        webTestClient.post()
                .uri("reactive/juiceWithIntermediateDTO")
                .bodyValue(new JuiceRequest("username-2", "apple-2", "orange-2"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("""
                        {
                            "juice": "Final juice"
                        }""");

    }
}
