package ru.tbcarus.jrfinal.client.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tbcarus.jrfinal.util.MillisToLocalDateDeserializer;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PolygonResponseTickerDataDto {
    private BigDecimal c;
    private BigDecimal h;
    private BigDecimal l;
    private int n;
    private BigDecimal o;
    @JsonDeserialize(using = MillisToLocalDateDeserializer.class)
    private LocalDate t;
    private long v;
    private BigDecimal vw;
}