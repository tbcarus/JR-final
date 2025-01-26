package ru.tbcarus.jrfinal.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PolygonResponseDto {
    private String ticker;
    private int queryCount;
    private int resultsCount;
    private boolean adjusted;
    private List<PolygonResponseTickerDataDto> results;
    private String status;
    @JsonProperty("request_id")
    private String requestId;
    private int count;
}