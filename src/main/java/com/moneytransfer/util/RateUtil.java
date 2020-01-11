package com.moneytransfer.util;

import com.moneytransfer.model.enums.Currency;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.math.BigDecimal;

public class RateUtil {
    public BigDecimal exchange(Currency currency, Currency currency1, BigDecimal amount) {
        HttpGet request = new HttpGet("https://api.exchangeratesapi.io/latest?base=USD");

        CloseableHttpClient httpClient = HttpClients.createDefault();

//        CloseableHttpResponse response = httpClient.execute(request);

        return null;
    }

    public static void main(String[] args) throws IOException {
        HttpGet request = new HttpGet("https://api.exchangeratesapi.io/latest?base=USD");

        CloseableHttpClient httpClient = HttpClients.createDefault();

        CloseableHttpResponse response = httpClient.execute(request);

        EntityUtils.toString(response.getEntity());
    }




}
