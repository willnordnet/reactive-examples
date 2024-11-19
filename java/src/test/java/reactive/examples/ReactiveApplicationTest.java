package reactive.examples;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;
import type.JuiceRequest;
import type.JuiceResponse;

import java.time.Duration;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


// Remove spring-boot-starter-web
@SpringBootTest(classes = ReactiveApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReactiveApplicationTest {

    private WebTestClient webTestClient;

    private long startTime;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:" + port)
                .responseTimeout(Duration.ofSeconds(15))
                .build();
        startTime = System.currentTimeMillis();

    }

    @AfterEach
    void afterEach() {
        System.out.println("======== Total time used: " + (System.currentTimeMillis() - startTime) + " ms");
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
    void order2Juice() throws InterruptedException {

        Runnable order = () -> webTestClient.post()
                .uri("reactive/juice")
                .bodyValue(new JuiceRequest("username-1", "apple-1", "orange-1"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("""
                        {
                            "juice": "Final juice"
                        }""");

        final Thread v1 = Thread.ofVirtual().start(order);
        final Thread v2 = Thread.ofVirtual().start(order);

        v1.join();
        v2.join();
    }

    // -XX:ActiveProcessorCount=1
    @Test
    void order5Juice() throws InterruptedException {

        Runnable order = () -> webTestClient.post()
                .uri("reactive/juice")
                .bodyValue(new JuiceRequest("username-1", "apple-1", "orange-1"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("""
                        {
                            "juice": "Final juice"
                        }""");

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
    }

    @Test
    void orderJuiceFlux() {
        webTestClient.post()
                .uri("reactive/flux/juice")
                .bodyValue(new JuiceRequest("username-1", "apple-5", "orange-5"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("""
                        [{"juice":"Final juice"},{"juice":"Final juice"},{"juice":"Final juice"},{"juice":"Final juice"},{"juice":"Final juice"}]""");

    }

    // -XX:ActiveProcessorCount=1
    @Test
    void prime5() throws InterruptedException {
        Runnable task = () -> webTestClient.post()
                .uri("reactive/prime")
                .bodyValue(1442968193)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .isEqualTo(true);

        final Thread v1 = Thread.ofVirtual().start(task);
        final Thread v2 = Thread.ofVirtual().start(task);
        final Thread v3 = Thread.ofVirtual().start(task);
        final Thread v4 = Thread.ofVirtual().start(task);
        final Thread v5 = Thread.ofVirtual().start(task);

        v1.join();
        v2.join();
        v3.join();
        v4.join();
        v5.join();
    }

    @Test
    void orderJuice2() {
        webTestClient.post()
                .uri("reactive/juice2")
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
    void getJuiceKt() {
        webTestClient.get()
                .uri("kt/reactive/juice")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("""
                        {
                            "juice":"Final juice"
                        }""");
    }

    @Test
    void getFlowJuiceKt() {
        webTestClient.get()
                .uri("kt/reactive/flow/juice")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("""
                        {
                            "juice":"Final juice"
                        }""");
    }

    @Test
    void order10Juice2() throws InterruptedException {
        IntStream.range(0, 10).forEach(i -> {
            String username = "username-" + i;
            Thread.ofVirtual().start(
                    () -> webTestClient.post()
                            .uri("reactive/juice2")
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
