package servlet.examples;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestClient;
import type.JuiceRequest;
import type.JuiceResponse;

import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest(classes = ServletApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
                .body(new JuiceRequest("username-1", "apple-1", "orange-1"))
                .retrieve()
                .toEntity(JuiceResponse.class)
                .getBody();

        assertThat(result.juice()).isEqualTo("Final juice");
    }

    @Test
    void orderJuiceCF() {
        final JuiceResponse result = restClient.post()
                .uri("servlet/cf/juice")
                .body(new JuiceRequest("username-1", "apple-1", "orange-1"))
                .retrieve()
                .toEntity(JuiceResponse.class)
                .getBody();

        assertThat(result.juice()).isEqualTo("Final juice");
    }

    @Test
    void orderJuiceVT() {
        final JuiceResponse result = restClient.post()
                .uri("servlet/vt/juice")
                .body(new JuiceRequest("username-1", "apple-1", "orange-1"))
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
                    .body(new JuiceRequest("username-1", "apple-1", "orange-1"))
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
    void orderJuice2() {
        final JuiceResponse result = restClient.post()
                .uri("servlet/juice")
                .body(new JuiceRequest("username-1", "apple-1", "orange-1"))
                .retrieve()
                .toEntity(JuiceResponse.class)
                .getBody();

        assertThat(result.juice()).isEqualTo("Final juice");

    }

    @Test
    void order10Juice2() throws InterruptedException {
        IntStream.range(0, 10).forEach(i -> {
            String username = "username-" + i;
            Thread.ofVirtual().start(() -> {
                final JuiceResponse result = restClient.post()
                        .uri("servlet/juice")
                        .body(new JuiceRequest(username, "apple-1", "orange-1"))
                        .retrieve()
                        .toEntity(JuiceResponse.class)
                        .getBody();

                assertThat(result.juice()).isEqualTo("Final juice");
            });
        });

        Thread.sleep(3000);
    }

}
