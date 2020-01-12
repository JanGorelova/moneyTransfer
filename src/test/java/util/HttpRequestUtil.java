package util;

import dto.ResponseDTO;
import io.vavr.control.Try;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpRequestUtil {
    public static ResponseDTO launchPost(String url, String json) {
        HttpPost request = new HttpPost(url);

        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        request.setEntity(Try.of(() -> new StringEntity(json)).get());

        return Try.withResources(() -> {
            CloseableHttpClient client = HttpClients.createDefault();
            return client.execute(request);

        }).of(HttpRequestUtil::toResponseDTO).get();
    }

    public static ResponseDTO launchGet(String url) {
        HttpGet request = new HttpGet(url);

        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");

        return Try.withResources(() -> {
            CloseableHttpClient client = HttpClients.createDefault();
            return client.execute(request);

        }).of(HttpRequestUtil::toResponseDTO).get();
    }

    private static ResponseDTO toResponseDTO(CloseableHttpResponse response) {
        String entity = Try.of(() -> EntityUtils.toString(response.getEntity())).get();

        return new ResponseDTO(entity, response.getStatusLine().getStatusCode());
    }
}
