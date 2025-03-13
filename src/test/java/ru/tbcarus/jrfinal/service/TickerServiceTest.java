package ru.tbcarus.jrfinal.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import ru.tbcarus.jrfinal.TickerTestData;
import ru.tbcarus.jrfinal.UserTestData;
import ru.tbcarus.jrfinal.client.PolygonServiceClient;
import ru.tbcarus.jrfinal.client.dto.PolygonResponseDto;
import ru.tbcarus.jrfinal.client.dto.PolygonResponseTickerDataDto;
import ru.tbcarus.jrfinal.exception.EntityNotFoundException;
import ru.tbcarus.jrfinal.exception.TickerRequestException;
import ru.tbcarus.jrfinal.model.Ticker;
import ru.tbcarus.jrfinal.model.TickerData;
import ru.tbcarus.jrfinal.model.User;
import ru.tbcarus.jrfinal.model.dto.TickerDataRequest;
import ru.tbcarus.jrfinal.model.dto.TickerDataResponse;
import ru.tbcarus.jrfinal.repository.TickerDataRepository;
import ru.tbcarus.jrfinal.repository.TickerRepository;
import ru.tbcarus.jrfinal.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static ru.tbcarus.jrfinal.TickerTestData.*;
import static ru.tbcarus.jrfinal.UserTestData.user1;
import static ru.tbcarus.jrfinal.UserTestData.user2;

@SpringBootTest
@TestPropertySource(locations = "classpath:.env.local-test")
class TickerServiceTest {

    @MockitoBean
    private PolygonServiceClient polygonServiceClient;

    @Autowired
    private TickerRepository tickerRepository;

    @Autowired
    private TickerDataRepository tickerDataRepository;

    @Autowired
    private TickerService tickerService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void cleanup() {
        user1.setId(null);
        user2.setId(null);
        tickerData1.setId(null);
        tickerData2.setId(null);
        tickerData3.setId(null);
    }

    @Test
    @Transactional
    @WithMockUser
    public void GetSavedTickerDataSuccess() {
        Ticker tickerAAPL = tickerRepository.findByName("AAPL").get();
        User user1 = userRepository.save(UserTestData.user1);
        User user2 = userRepository.save(UserTestData.user2);
        TickerData td1 = TickerTestData.tickerData1;
        td1.setUser(user1);
        td1.setTicker(tickerAAPL);
        tickerDataRepository.save(td1);
        TickerData td2 = TickerTestData.tickerData2;
        td2.setUser(user1);
        td2.setTicker(tickerAAPL);
        tickerDataRepository.save(td2);
        TickerData td3 = TickerTestData.tickerData3;
        td3.setUser(user2);
        td3.setTicker(tickerAAPL);
        tickerDataRepository.save(td3);

        TickerDataResponse savedTickerData = tickerService.getSavedTickerData(user1, tickerAAPL.getName());
        assertEquals(tickerAAPL.getName(), savedTickerData.getTicker());
        assertEquals(2, savedTickerData.getData().size());
    }

    @Test
    @Transactional
    @WithMockUser
    public void GetSavedTickerDataTickerNotFound() {
        User user1 = userRepository.save(UserTestData.user1);

        assertThrows(EntityNotFoundException.class, () -> {
            tickerService.getSavedTickerData(user1, tickerNE.getName());
        });
    }

    @Test
    @Transactional
    @WithMockUser
    public void TickerRequestAndSaveProcessorSuccess() {
        User user1 = userRepository.save(UserTestData.user1);
        TickerDataRequest tdr = tickerDataRequest;

        Ticker tickerAAPL = tickerRepository.findByName("AAPL").get();
        TickerDataResponse savedTickerData = tickerService.getSavedTickerData(user1, tickerAAPL.getName());
        assertEquals(0, savedTickerData.getData().size());

        TickerData td1 = TickerTestData.tickerData1;
        td1.setUser(user1);
        td1.setTicker(tickerAAPL);
        tickerDataRepository.save(td1);
        TickerData td2 = TickerTestData.tickerData2;
        td2.setUser(user1);
        td2.setTicker(tickerAAPL);
        tickerDataRepository.save(td2);

        savedTickerData = tickerService.getSavedTickerData(user1, tickerAAPL.getName());
        assertEquals(2, savedTickerData.getData().size());

        List<PolygonResponseTickerDataDto> polygonResponseList = List.of(prDto1, prDto2, prDto3, prDto4);
        PolygonResponseDto polygonResponseDto = PolygonResponseDto.builder()
                .results(polygonResponseList)
                .build();

        when(polygonServiceClient.getTickerRecords(eq(tdr.getTicker()), eq(tdr.getStart().toString()), eq(tdr.getEnd().toString()), anyString()))
                .thenReturn(polygonResponseDto);
        tickerService.tickerRequestAndSaveProcessor(user1, tdr);

        savedTickerData = tickerService.getSavedTickerData(user1, tickerAAPL.getName());
        assertEquals(4, savedTickerData.getData().size());
    }

    @Test
    @WithMockUser
    @Transactional
    public void TickerRequestAndSaveProcessor_InvalidDateRange() {
        User user1 = userRepository.save(UserTestData.user1);
        assertThrows(TickerRequestException.class, () -> {
            tickerService.tickerRequestAndSaveProcessor(user1, tickerDataRequestWrong);
        });
    }

}