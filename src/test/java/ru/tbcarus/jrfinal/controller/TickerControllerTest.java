package ru.tbcarus.jrfinal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.tbcarus.jrfinal.TickerTestData;
import ru.tbcarus.jrfinal.model.User;
import ru.tbcarus.jrfinal.model.dto.TickerDataRequest;
import ru.tbcarus.jrfinal.service.TickerService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:.env.local-test")
class TickerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TickerService tickerService;

    @Test
    @WithMockUser
    void tickerRequestAndSave() throws Exception {
        doNothing().when(tickerService).tickerRequestAndSaveProcessor(any(User.class), any(TickerDataRequest.class));
        mockMvc.perform(post(TickerController.TICKER_DATA_REQUEST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TickerTestData.tickerDataRequest)))
                .andExpect(status().isCreated());

    }

    @Test
    @WithMockUser
    void getSavedTickerData() throws Exception {
        mockMvc.perform(get(TickerController.TICKER_GET_SAVED_URL)
                        .param("ticker", "AAPL")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}