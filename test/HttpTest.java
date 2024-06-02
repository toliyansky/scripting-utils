import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;

import static scripting.Utils.*;

public class HttpTest {
    public void testHttpDefaultGet() {
        var response = http.get("https://httpbin.org/get");

        if (response.statusCode() != 200) {
            System.err.println("[httpTest] failed");
            System.err.println(STR."Expected: 200");
            System.err.println(STR."Got: \{response.statusCode()}");
        }
    }

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

        println(response.body());
        println(response.statusCode());

        if (response.statusCode() != 200 || !response.body().contains(STR."John Doe") || !response.body().contains(STR."\{id}")) {
            System.err.println("[testHttpPost] failed");
            System.err.println(STR."Expected code: 200");
            System.err.println(STR."Got: \{response.statusCode()}\n");
            System.err.println(STR."Expected name: 'John Doe'");
            System.err.println(STR."Expected id: '\{id}'");
            System.err.println(STR."Got body: '\{response.body()}'");
        }
    }
}
