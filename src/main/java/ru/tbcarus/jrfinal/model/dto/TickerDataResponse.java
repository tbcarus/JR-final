package ru.tbcarus.jrfinal.model.dto;

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
public class TickerDataResponse {

    private final UUID id = UUID.randomUUID();
    private String ticker;
    private List<TickerDataDto> data;
}

