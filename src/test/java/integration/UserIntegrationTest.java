package integration;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;
import util.ApplicationInitializationUtil;
import util.DataGenerationUtil;

import java.io.IOException;


public class UserIntegrationTest {
    private static final String USER_CREATION_URL = "http://localhost:8081/api/users/create";


    @Before
    public void setUp() {
        ApplicationInitializationUtil.initialize();
    }

    @Test
    public void testUserSuccessfullyCreated() throws Exception{
        String json = DataGenerationUtil.userCreationDTOJson();
        HttpPost request = new HttpPost(USER_CREATION_URL);

        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");

        request.setEntity(new StringEntity(json));

        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(request);
        String entity = EntityUtils.toString(response.getEntity());

    }

    @Test
    public void testUserCreationFailedEmailAlreadyExists() throws IOException {

    }
}
