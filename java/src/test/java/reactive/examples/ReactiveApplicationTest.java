package reactive.examples;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;
import type.JuiceRequest;
import type.JuiceResponse;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


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
    void blend2Fruits() {
        long startTime = System.currentTimeMillis();

        webTestClient.post()
                .uri("reactive/blend")
                .bodyValue(new JuiceRequest("username-1", "apple-1", "orange-1"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("""
                        {
                            "juice": "Final juice"
                        }""");
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Total time used: " + totalTime + " ms");
    }

    @Test
    void order2Juice() throws InterruptedException {

        Runnable order = () -> webTestClient.post()
                .uri("reactive/blend")
                .bodyValue(new JuiceRequest("username-1", "apple-1", "orange-1"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("""
                        {
                            "juice": "Final juice"
                        }""");

        long startTime = System.currentTimeMillis();

        final Thread v1 = Thread.ofVirtual().start(order);
        final Thread v2 = Thread.ofVirtual().start(order);

        v1.join();
        v2.join();

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Total time used: " + totalTime + " ms");
    }

    @Test
    void order5Juice() throws InterruptedException {

        Runnable order = () -> webTestClient.post()
                .uri("reactive/blend")
                .bodyValue(new JuiceRequest("username-1", "apple-1", "orange-1"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("""
                        {
                            "juice": "Final juice"
                        }""");

        long startTime = System.currentTimeMillis();

        final Thread v1 = Thread.ofVirtual().start(order);
        final Thread v2 = Thread.ofVirtual().start(order);
        final Thread v3 = Thread.ofVirtual().start(order);
        final Thread v4 = Thread.ofVirtual().start(order);
        final Thread v5 = Thread.ofVirtual().start(order);

        v1.join();
        v2.join();
        v3.join();
        v4.join();
        v5.join();

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Total time used: " + totalTime + " ms");
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
        final JuiceResponse result = webTestClient.post()
                .uri("kt/reactive/juice")
                .bodyValue(new JuiceRequest("username-1", "apple-1", "orange-1"))
                .exchange().returnResult(JuiceResponse.class).getResponseBody().blockFirst();

        assertThat(result.juice()).isEqualTo("Final juice");
    }

    @Test
    void getHelloWorldKt() {
        webTestClient.get()
                .uri("kt/reactive/get")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .equals("Hello, World!");
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
