package com.moneytransfer.model.dto.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Getter
public class RateDTO {
    @JsonProperty(value="rates")
    Map<String, BigDecimal> rates;

    @JsonProperty(value="base")
    String base;

    @JsonProperty(value="date")
    LocalDate date;
}
