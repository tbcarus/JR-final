package ru.tbcarus.jrfinal.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.tbcarus.jrfinal.client.dto.PolygonResponseDto;

import java.time.LocalDate;

@FeignClient(name = "polygon-api", url = "${client.polygon.url}")
public interface PolygonServiceClient {

    @GetMapping("/v2/aggs/ticker/{stocksTicker}/range/1/day/{from}/{to}")
    PolygonResponseDto getTickerRecords(@PathVariable String stocksTicker, @PathVariable String from, @PathVariable String to, @RequestParam String apiKey);

}