package ru.tbcarus.jrfinal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.tbcarus.jrfinal.model.User;
import ru.tbcarus.jrfinal.model.dto.TickerDataRequest;
import ru.tbcarus.jrfinal.model.dto.TickerDataResponse;
import ru.tbcarus.jrfinal.service.TickerService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Work with ticker data")
public class TickerController {
    private final TickerService tickerService;

    public static final String TICKER_DATA_REQUEST_URL = "/api/user/save";
    public static final String TICKER_GET_SAVED_URL = "/api/user/saved";

    @Operation(summary = "Request ticker data from third party api and save new")
    @PostMapping(TICKER_DATA_REQUEST_URL)
    public ResponseEntity<Void> tickerRequestAndSave(@AuthenticationPrincipal User user, @RequestBody TickerDataRequest tickerDataRequest) {
        tickerService.tickerRequestAndSaveProcessor(user, tickerDataRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Take all saved data by ticker")
    @GetMapping(TICKER_GET_SAVED_URL)
    public ResponseEntity<TickerDataResponse> getSavedTickerData(@AuthenticationPrincipal User user, @RequestParam String ticker) {
        TickerDataResponse savedTickerData = tickerService.getSavedTickerData(user, ticker);
        return ResponseEntity.status(HttpStatus.OK).body(savedTickerData);
    }
}
