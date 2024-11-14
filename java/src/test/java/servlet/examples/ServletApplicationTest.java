package servlet.examples;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestClient;
import reactive.examples.type.JuiceRequest;
import reactive.examples.type.JuiceResponse;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest(classes = ServletApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ServletApplicationTest {


    private RestClient restClient;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        restClient = RestClient.builder().baseUrl("http://localhost:" + port).build();
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

//    @Test
//    void order10Juice() throws InterruptedException {
//        IntStream.range(0, 10).forEach(i -> {
//            String username = "username-" + i;
//            Thread.ofVirtual().start(() -> webTestClient.post()
//                    .uri("servlet/juice")
//                    .bodyValue(new JuiceRequest(username, "apple-1", "orange-1"))
//                    .exchange()
//                    .expectStatus().isOk()
//                    .expectBody()
//                    .json("""
//                            {
//                                "juice": "Final juice"
//                            }"""));
//        });
//
//        Thread.sleep(3000);
//    }

}
