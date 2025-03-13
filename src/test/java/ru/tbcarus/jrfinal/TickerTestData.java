package ru.tbcarus.jrfinal;

import ru.tbcarus.jrfinal.client.dto.PolygonResponseTickerDataDto;
import ru.tbcarus.jrfinal.model.Ticker;
import ru.tbcarus.jrfinal.model.TickerData;
import ru.tbcarus.jrfinal.model.dto.TickerDataRequest;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TickerTestData {
    public static TickerDataRequest tickerDataRequest = TickerDataRequest.builder()
            .ticker("AAPL")
            .start(LocalDate.of(2025, 1, 1))
            .end(LocalDate.of(2025, 1, 10))
            .build();

    public static TickerDataRequest tickerDataRequestWrong = TickerDataRequest.builder()
            .ticker("AAPL")
            .start(LocalDate.of(2025, 1, 10))
            .end(LocalDate.of(2025, 1, 1))
            .build();

    public static Ticker tickerAAPL = Ticker.builder()
            .name("AAPL")
            .companyName("Apple inc.")
            .build();

    public static Ticker tickerGOOGL = Ticker.builder()
            .name("GOOGl")
            .companyName("Alphabet Inc.")
            .build();

    public static Ticker tickerNE = Ticker.builder()
            .name("NE")
            .companyName("Not exist Inc.")
            .build();

    public static TickerData tickerData1 = TickerData.builder()
            .date(LocalDate.of(2025, 1, 1))
            .low(BigDecimal.valueOf(1))
            .high(BigDecimal.valueOf(2))
            .open(BigDecimal.valueOf(0.5))
            .close(BigDecimal.valueOf(1.51))
            .build();
    public static TickerData tickerData2 = TickerData.builder()
            .date(LocalDate.of(2025, 1, 10))
            .low(BigDecimal.valueOf(1))
            .high(BigDecimal.valueOf(2))
            .open(BigDecimal.valueOf(0.5))
            .close(BigDecimal.valueOf(1.52))
            .build();
    public static TickerData tickerData3 = TickerData.builder()
            .date(LocalDate.of(2025, 1, 5))
            .low(BigDecimal.valueOf(1))
            .high(BigDecimal.valueOf(2))
            .open(BigDecimal.valueOf(0.5))
            .close(BigDecimal.valueOf(1.53))
            .build();
    public static TickerData tickerData4 = TickerData.builder()
            .date(LocalDate.of(2025, 1, 8))
            .low(BigDecimal.valueOf(1))
            .high(BigDecimal.valueOf(2))
            .open(BigDecimal.valueOf(0.5))
            .close(BigDecimal.valueOf(1.54))
            .build();

    public static PolygonResponseTickerDataDto prDto1 = TickerDataToPRTDD(tickerData1);
    public static PolygonResponseTickerDataDto prDto2 = TickerDataToPRTDD(tickerData2);
    public static PolygonResponseTickerDataDto prDto3 = TickerDataToPRTDD(tickerData3);
    public static PolygonResponseTickerDataDto prDto4 = TickerDataToPRTDD(tickerData4);

    private static PolygonResponseTickerDataDto TickerDataToPRTDD(TickerData td) {
        PolygonResponseTickerDataDto pr = PolygonResponseTickerDataDto.builder()
                .t(td.getDate())
                .l(td.getLow())
                .h(td.getHigh())
                .o(td.getOpen())
                .c(td.getClose())
                .build();
        return pr;
    }


}
