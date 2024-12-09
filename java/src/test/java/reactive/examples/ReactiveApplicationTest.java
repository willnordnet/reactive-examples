package reactive.examples;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;
import type.JuiceRequest;
import type.JuiceResponse;

import java.time.Duration;


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
                .bodyValue(new JuiceRequest(1, 1))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("""
                        {
                            "juice": "Juice with Apple juice and Orange juice"
                        }""");
    }

    // webflux does not support blocking
    @Test
    void orderBlockJuice() {
        webTestClient.post()
                .uri("reactive/blockJuice")
                .bodyValue(new JuiceRequest(1, 1))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("""
                        {
                            "juice": "Juice with Apple juice and Orange juice"
                        }""");
    }

    @Test
    void orderSleepJuice() {
        webTestClient.post()
                .uri("reactive/sleepJuice")
                .bodyValue(new JuiceRequest(1, 1))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("""
                        {
                            "juice": "Juice with Apple juice and Orange juice"
                        }""");
    }

    @Test
    void order2Juice() throws InterruptedException {
        Runnable order = () -> webTestClient.post()
                .uri("reactive/juice")
                .bodyValue(new JuiceRequest(1, 1))
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
                .bodyValue(new JuiceRequest(1, 1))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("""
                        {
                            "juice": "Juice with Apple juice and Orange juice"
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
    void order5BlockJuice() throws InterruptedException {
        Runnable order = () -> webTestClient.post()
                .uri("reactive/blockJuice")
                .bodyValue(new JuiceRequest(1, 1))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("""
                        {
                            "juice": "Juice with Apple juice and Orange juice"
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
    void order5Block5ReactiveJuice() throws InterruptedException {
        Runnable blockingOrder = () -> webTestClient.post()
                .uri("reactive/blockJuice")
                .bodyValue(new JuiceRequest(1, 1))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("""
                        {
                            "juice": "Juice with Apple juice and Orange juice"
                        }""");

        Runnable reactiveOrder = () -> webTestClient.post()
                .uri("reactive/juice")
                .bodyValue(new JuiceRequest(1, 1))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("""
                        {
                            "juice": "Juice with Apple juice and Orange juice"
                        }""");


        final Thread v1 = Thread.ofVirtual().start(blockingOrder);
        final Thread v2 = Thread.ofVirtual().start(blockingOrder);
//        final Thread v3 = Thread.ofVirtual().start(blockingOrder);
//        final Thread v4 = Thread.ofVirtual().start(blockingOrder);
//        final Thread v5 = Thread.ofVirtual().start(blockingOrder);

        final Thread r1 = Thread.ofVirtual().start(reactiveOrder);
        final Thread r2 = Thread.ofVirtual().start(reactiveOrder);
//        final Thread r3 = Thread.ofVirtual().start(reactiveOrder);
//        final Thread r4 = Thread.ofVirtual().start(reactiveOrder);
//        final Thread r5 = Thread.ofVirtual().start(reactiveOrder);

        v1.join();
        v2.join();
//        v3.join();
//        v4.join();
//        v5.join();

        r1.join();
        r2.join();
//        r3.join();
//        r4.join();
//        r5.join();
    }

    @Test
    void order5SleepJuice() throws InterruptedException {
        Runnable order = () -> webTestClient.post()
                .uri("reactive/sleepJuice")
                .bodyValue(new JuiceRequest(1, 1))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("""
                        {
                            "juice": "Juice with Apple juice and Orange juice"
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
    void primeJuice() throws InterruptedException {
        Runnable task = () -> webTestClient.post()
                .uri("reactive/primeJuice")
                .bodyValue(1442968193)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .isEqualTo(true);

        final Thread v1 = Thread.ofVirtual().start(task);

        v1.join();
    }

    // -XX:ActiveProcessorCount=1
    @Test
    void prime5Juice() throws InterruptedException {
        Runnable task = () -> webTestClient.post()
                .uri("reactive/primeJuice")
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
    void orderJuiceInBatch() {
        webTestClient.post()
                .uri("reactive/flux/juice")
                .bodyValue(new JuiceRequest(5, 5))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("""
                        [
                          {
                            "juice": "Juice with apple 0 and orange 0"
                          },
                          {
                            "juice": "Juice with apple 1 and orange 1"
                          },
                          {
                            "juice": "Juice with apple 2 and orange 2"
                          },
                          {
                            "juice": "Juice with apple 3 and orange 3"
                          },
                          {
                            "juice": "Juice with apple 4 and orange 4"
                          }
                        ]""");
    }

    @Test
    void orderJuiceInStream() {
        StepVerifier.create(webTestClient.post()
                        .uri("reactive/flux/juice")
                        .bodyValue(new JuiceRequest("username-1", 5, 5))
                        .exchange()
                        .returnResult(JuiceResponse.class)
                        .getResponseBody())
                .consumeNextWith(juice -> System.out.println("Received a " + juice))
                .consumeNextWith(juice -> System.out.println("Received a " + juice))
                .consumeNextWith(juice -> System.out.println("Received a " + juice))
                .consumeNextWith(juice -> System.out.println("Received a " + juice))
                .consumeNextWith(juice -> System.out.println("Received a " + juice))
                .verifyComplete();
    }

    @Test
    void order5JuiceInStream() throws InterruptedException {
        Runnable order = () -> StepVerifier.create(webTestClient.post()
                        .uri("reactive/flux/juice")
                        .bodyValue(new JuiceRequest(5, 5))
                        .exchange()
                        .returnResult(JuiceResponse.class)
                        .getResponseBody())
                .consumeNextWith(juice -> System.out.println("Received a " + juice))
                .consumeNextWith(juice -> System.out.println("Received a " + juice))
                .consumeNextWith(juice -> System.out.println("Received a " + juice))
                .consumeNextWith(juice -> System.out.println("Received a " + juice))
                .consumeNextWith(juice -> System.out.println("Received a " + juice))
                .verifyComplete();

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
}
