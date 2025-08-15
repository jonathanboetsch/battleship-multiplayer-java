package se.boetsch.Battleship;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    class ServerStatusTest {
        @LocalServerPort
        int port;
        @Test
        void health_is_ok() throws Exception {
            var body = new RestTemplate().getForObject("http://localhost:"+port+"/health", String.class);
            assertNotNull(body);
        }
    }
