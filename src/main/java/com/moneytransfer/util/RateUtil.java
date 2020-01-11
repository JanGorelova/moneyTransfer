package com.moneytransfer.util;

import com.moneytransfer.exception.MoneyTransferException;
import com.moneytransfer.model.dto.entity.RateDTO;
import com.moneytransfer.model.enums.Currency;
import io.javalin.plugin.json.JavalinJson;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.eclipse.jetty.http.HttpStatus;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Optional;

@Slf4j
public class RateUtil {
    private static final String EXCHANGE_RATE_URL = "https://api.exchangeratesapi.io/latest";

    public static BigDecimal exchange(Currency exchangeTo, Currency exchangeFrom, BigDecimal amount) {
        if (exchangeFrom.equals(exchangeTo))
            return amount;

        URI uri = buildURI(exchangeFrom);

        HttpGet request = new HttpGet(uri);
        String entity = Try.withResources(() -> HttpClients.createDefault().execute(request))
                .of(response -> EntityUtils.toString(response.getEntity()))
                .onFailure(ex -> log.error(ex.getMessage())).get();

        RateDTO rateDTO = JavalinJson.fromJson(entity, RateDTO.class);

        return Optional.ofNullable(rateDTO).map(RateDTO::getRates).map(rates -> rates.get(exchangeTo.name()))
                .map(rate -> rate.multiply(amount))
                .orElseThrow(() -> new MoneyTransferException("Rate was not found", HttpStatus.INTERNAL_SERVER_ERROR_500));
    }

    private static URI buildURI(Currency exchangeFrom) {
        return Try.of(() -> {
            URIBuilder builder = new URIBuilder(EXCHANGE_RATE_URL);
            builder.setParameter("base", exchangeFrom.name());

            return builder.build();
        }).get();
    }
}
