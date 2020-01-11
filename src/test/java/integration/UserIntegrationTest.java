package integration;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Before;
import org.junit.Test;
import util.ApplicationInitializationUtil;

import java.io.IOException;


public class UserIntegrationTest {
    @Before
    public void setUp() {
        ApplicationInitializationUtil.initialize();
    }

    @Test
    public void testUserSuccessfullyCreated() throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost("http://localhost:8081/api/users/create");

        String json = " {\n" +
                "\t\"firstName\" : \"tets\",\n" +
                "\t\"lastName\": \"ytrt\",\n" +
                "\t\"email\": \"janny@gn5em.com\"\n" +
                "}";
        request.setEntity(new StringEntity(json));

        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");

        client.execute(request);
    }
}
