package com.digitalheroes.pagepulse.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.digitalheroes.pagepulse.dto.AuditResponse;
import com.digitalheroes.pagepulse.service.AuditService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuditController.class)
class AuditControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuditService auditService;

    @Test
    void auditReturnsReport() throws Exception {
        when(auditService.audit(any())).thenReturn(AuditResponse.builder()
                .status(200)
                .responseTime(123L)
                .title("OpenAI")
                .metaDescription("Creating safe AGI")
                .h1Count(2)
                .missingAltImages(3)
                .wordCount(1685L)
                .build());

        mockMvc.perform(post("/api/audit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"url\":\"https://openai.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.title").value("OpenAI"));
    }
}