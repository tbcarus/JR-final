package ru.tbcarus.jrfinal;

import ru.tbcarus.jrfinal.model.dto.TickerDataRequest;

import java.time.LocalDate;

public class TickerTestData {
    public static TickerDataRequest tickerDataRequest = TickerDataRequest.builder()
            .ticker("AAPL")
            .start(LocalDate.now().minusMonths(1))
            .end(LocalDate.now())
            .build();
}
