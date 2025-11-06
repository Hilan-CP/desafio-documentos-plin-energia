package com.plin.documents.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plin.documents.dto.DocumentUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class DocumentControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;
    private MockMultipartFile file;
    private DocumentUrl documentUrl;

    @BeforeEach
    void setUp(){
        existingId = 1L;
        nonExistingId = 1000L;
        file = new MockMultipartFile("file", "arquivo.pdf", "application/pdf", "conteudo falso".getBytes());
        documentUrl = new DocumentUrl("https://www.google.com.br");
    }

    @Test
    public void getDocumentsByClientIdShouldReturnOKWhenExistingId() throws Exception {
        mockMvc.perform(get("/documents/client/{clientId}", existingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].title").exists())
                .andExpect(jsonPath("$[0].creationDate").exists());
    }

    @Test
    public void getDocumentsByClientIdShouldReturnOKWhenNonExistingId() throws Exception {
        mockMvc.perform(get("/documents/client/{clientId}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void downloadDocumentShouldReturnOKWhenExistingId() throws Exception {
        mockMvc.perform(get("/documents/download/{id}", existingId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));
    }

    @Test
    public void downloadDocumentShouldReturnNotFoundWhenNonExistingId() throws Exception {
        mockMvc.perform(get("/documents/download/{id}", nonExistingId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createDocumentFromUploadShouldReturnCreatedWhenExistingClientId() throws Exception {
        mockMvc.perform(multipart(HttpMethod.POST, "/documents/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("X-Client-Id", existingId))
                .andExpect(status().isCreated());
    }

    @Test
    public void createDocumentFromUploadShouldReturnNotFoundWhenNonExistingClientId() throws Exception {
        mockMvc.perform(multipart(HttpMethod.POST, "/documents/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("X-Client-Id", nonExistingId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createDocumentFromUrlShouldReturnOKWhenExistingClientId() throws Exception {
        String json = objectMapper.writeValueAsString(documentUrl);
        mockMvc.perform(post("/documents/url")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("X-Client-Id", existingId))
                .andExpect(status().isCreated());
    }

    @Test
    public void createDocumentFromUrlShouldReturnNotFoundWhenNonExistingClientId() throws Exception {
        String json = objectMapper.writeValueAsString(documentUrl);
        mockMvc.perform(post("/documents/url")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("X-Client-Id", nonExistingId))
                .andExpect(status().isNotFound());
    }
}
