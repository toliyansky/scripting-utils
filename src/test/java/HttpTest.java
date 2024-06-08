import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static scripting.Utils.*;

public class HttpTest {

    @Test
    public void testHttpDefaultGet() {
        var response = http.get("https://httpbin.org/get");
        assertEquals(200, response.statusCode());
    }

    @Test
    public void testHttpPost() {
        int id = new Random().nextInt(1000);
        var response = http.request(
                HttpRequest.newBuilder()
                        .POST(HttpRequest.BodyPublishers.ofString(STR."""
                                {
                                    "id": \{id},
                                    "name": "John Doe"
                                }"""))
                        .header("Content-Type", "application/json")
                        .uri(URI.create("https://httpbin.org/post")),
                HttpResponse.BodyHandlers.ofString()
        );

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("John Doe"));
        assertTrue(response.body().contains(String.valueOf(id)));
    }
}
