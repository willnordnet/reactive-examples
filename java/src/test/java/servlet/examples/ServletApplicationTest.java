package servlet.examples;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import type.JuiceRequest;
import type.JuiceResponse;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest(classes = ServletApplication.class,
        properties = {
                "server.tomcat.threads.max=1",
                "spring.threads.virtual.enabled=false"
        },
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ServletApplicationTest {


    private RestClient restClient;

    @LocalServerPort
    private int port;

    private long startTime;


    @BeforeEach
    void setUp() {
        restClient = RestClient.builder().baseUrl("http://localhost:" + port).build();
        startTime = System.currentTimeMillis();
    }

    @AfterEach
    void afterEach() {
        System.out.println("======== Total time used: " + (System.currentTimeMillis() - startTime) + " ms");
    }

    @Test
    void orderJuice() {
        final JuiceResponse result = restClient.post()
                .uri("servlet/juice")
                .body(new JuiceRequest(1, 1))
                .retrieve()
                .toEntity(JuiceResponse.class)
                .getBody();

        assertThat(result.juice()).isEqualTo("Final juice");
    }

    @Test
    void orderJuiceCF() {
        final JuiceResponse result = restClient.post()
                .uri("servlet/cf/juice")
                .body(new JuiceRequest(1, 1))
                .retrieve()
                .toEntity(JuiceResponse.class)
                .getBody();

        assertThat(result.juice()).isEqualTo("Final juice");
    }

    @Test
    void orderJuiceVT() {
        final JuiceResponse result = restClient.post()
                .uri("servlet/vt/juice")
                .body(new JuiceRequest(1, 1))
                .retrieve()
                .toEntity(JuiceResponse.class)
                .getBody();

        assertThat(result.juice()).isEqualTo("Final juice");
    }


    // Note: The num of tomcat thread pool size only takes effect when it's a servlet application
    @Test
    void order2JuiceVT() throws InterruptedException {
        final Runnable orderJuice = () -> {
            final JuiceResponse result = restClient.post()
                    .uri("servlet/vt/juice")
                    .body(new JuiceRequest(1, 1))
                    .retrieve()
                    .toEntity(JuiceResponse.class)
                    .getBody();

            assertThat(result.juice()).isEqualTo("Final juice");
        };

        final Thread vt1 = Thread.ofVirtual().start(orderJuice);
        final Thread vt2 = Thread.ofVirtual().start(orderJuice);
        vt1.join();
        vt2.join();
    }

    @Test
    void order5JuiceVT() throws InterruptedException {
        final Runnable orderJuice = () -> {
            final JuiceResponse result = restClient.post()
                    .uri("servlet/vt/juice")
                    .body(new JuiceRequest(1, 1))
                    .retrieve()
                    .toEntity(JuiceResponse.class)
                    .getBody();

            assertThat(result.juice()).isEqualTo("Final juice");
        };

        final Thread vt1 = Thread.ofVirtual().start(orderJuice);
        final Thread vt2 = Thread.ofVirtual().start(orderJuice);
        final Thread vt3 = Thread.ofVirtual().start(orderJuice);
        final Thread vt4 = Thread.ofVirtual().start(orderJuice);
        final Thread vt5 = Thread.ofVirtual().start(orderJuice);
        vt1.join();
        vt2.join();
        vt3.join();
        vt4.join();
        vt5.join();
    }

    @Test
    void order5PrimeJuice() throws InterruptedException {
        final Runnable orderJuice = () -> {
            final HttpStatusCode statusCode = restClient.post()
                    .uri("servlet/primeJuice")
                    .body(1442968193)
                    .retrieve()
                    .toBodilessEntity()
                    .getStatusCode();
            assertThat(statusCode).isEqualTo(HttpStatus.OK);
        };

        final Thread vt1 = Thread.ofVirtual().start(orderJuice);
        final Thread vt2 = Thread.ofVirtual().start(orderJuice);
        final Thread vt3 = Thread.ofVirtual().start(orderJuice);
        final Thread vt4 = Thread.ofVirtual().start(orderJuice);
        final Thread vt5 = Thread.ofVirtual().start(orderJuice);
        vt1.join();
        vt2.join();
        vt3.join();
        vt4.join();
        vt5.join();
    }


}
