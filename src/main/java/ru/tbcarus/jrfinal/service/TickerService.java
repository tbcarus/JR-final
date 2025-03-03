package ru.tbcarus.jrfinal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.tbcarus.jrfinal.client.PolygonServiceClient;
import ru.tbcarus.jrfinal.client.dto.PolygonResponseDto;
import ru.tbcarus.jrfinal.client.dto.PolygonResponseTickerDataDto;
import ru.tbcarus.jrfinal.exception.EntityNotFoundException;
import ru.tbcarus.jrfinal.exception.TickerRequestException;
import ru.tbcarus.jrfinal.model.Ticker;
import ru.tbcarus.jrfinal.model.TickerData;
import ru.tbcarus.jrfinal.model.User;
import ru.tbcarus.jrfinal.model.dto.TickerDataDto;
import ru.tbcarus.jrfinal.model.dto.mapper.TickerDataMapper;
import ru.tbcarus.jrfinal.model.dto.TickerDataRequest;
import ru.tbcarus.jrfinal.model.dto.TickerDataResponse;
import ru.tbcarus.jrfinal.repository.TickerDataRepository;
import ru.tbcarus.jrfinal.repository.TickerRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TickerService {

    private final PolygonServiceClient polygonServiceClient;
    private final TickerRepository tickerRepository;
    private final TickerDataRepository tickerDataRepository;
    private final TickerDataMapper tickerDataMapper;

    @Value("${client.polygon.key}")
    private String polygonKey;

    public TickerDataResponse getSavedTickerData(User user, String tickerName) {
        Ticker ticker = checkTicker(tickerName);
        List<TickerDataDto> tickerDataDtoList = tickerDataRepository.findAllByUserAndTicker(user, ticker).stream()
                        .map(tickerDataMapper::toDto)
                        .toList();
        return TickerDataResponse.builder()
                .ticker(ticker.getName())
                .data(tickerDataDtoList)
                .build();
    }

    public void tickerRequestAndSaveProcessor(User user, TickerDataRequest tdr) {
        Ticker ticker = checkRequest(tdr);;
        List<TickerData> localTickerData = tickerDataRepository.findByUserAndTickerAndDateBetween(user, ticker, tdr.getStart(), tdr.getEnd());
        List<PolygonResponseTickerDataDto> polygonTickerData = getTickersRecords(tdr).getResults();
        if (localTickerData.size() == polygonTickerData.size()) {
            return;
        }

        List<PolygonResponseTickerDataDto> filteredPolygonTickerData = filterData(localTickerData, polygonTickerData);
        if (!filteredPolygonTickerData.isEmpty()) {
            List<TickerData> tickersToSave = filteredPolygonTickerData.stream()
                    .map(ptd -> mapToTickerData(user, ticker, ptd))
                    .toList();
            tickerDataRepository.saveAll(tickersToSave);
        }
    }

    private Ticker checkTicker(String tickerName) {
        Optional<Ticker> ticker = tickerRepository.findByName(tickerName);
        if (ticker.isEmpty()) {
            throw new EntityNotFoundException(tickerName, String.format("Ticker with name %s not found in local base", tickerName));
        }
        return ticker.get();
    }

    private Ticker checkRequest(TickerDataRequest tdr) {
        if (tdr.getStart().isAfter(tdr.getEnd())) {
            throw new TickerRequestException("Start date cannot be after end date");
        }
        return checkTicker(tdr.getTicker());
    }

    public List<PolygonResponseTickerDataDto> filterData(List<TickerData> local, List<PolygonResponseTickerDataDto> polygon) {
        List<LocalDate> dates = local.stream().map(TickerData::getDate).toList();
        return polygon.stream().filter(polygonTickerData -> !dates.contains(polygonTickerData.getT())).toList();
    }

    public TickerData mapToTickerData(User user, Ticker ticker, PolygonResponseTickerDataDto ptd) {
        return TickerData.builder()
                .date(ptd.getT())
                .open(ptd.getO())
                .close(ptd.getC())
                .high(ptd.getH())
                .low(ptd.getL())
                .user(user)
                .ticker(ticker)
                .build();
    }

    public PolygonResponseDto getTickersRecords(TickerDataRequest tdr) {
        return polygonServiceClient.getTickerRecords(tdr.getTicker(), tdr.getStart().toString(), tdr.getEnd().toString(), polygonKey);
    }
}
