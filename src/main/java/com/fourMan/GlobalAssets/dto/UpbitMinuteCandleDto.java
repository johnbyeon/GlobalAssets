package com.fourMan.GlobalAssets.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record UpbitMinuteCandleDto (
        String market,
        @JsonProperty("candle_date_time_utc") String candleDateTimeUtc,
        @JsonProperty("candle_date_time_kst") String candleDateTimeKst,
        @JsonProperty("opening_price") BigDecimal openingPrice,
        @JsonProperty("high_price") BigDecimal highPrice,
        @JsonProperty("low_price") BigDecimal lowPrice,
        @JsonProperty("trade_price") BigDecimal tradePrice,
        @JsonProperty("candle_acc_trade_price") BigDecimal accTradePrice,
        @JsonProperty("candle_acc_trade_volume") BigDecimal accTradeVolume,
        long timestamp,
        int unit
) {}
