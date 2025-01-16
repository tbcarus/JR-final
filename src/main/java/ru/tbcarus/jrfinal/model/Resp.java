package ru.tbcarus.jrfinal.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resp {

    private final UUID uuid = UUID.randomUUID();
    private Ticker ticker;
    private List<TickerDayData> tickerDayData;
}
