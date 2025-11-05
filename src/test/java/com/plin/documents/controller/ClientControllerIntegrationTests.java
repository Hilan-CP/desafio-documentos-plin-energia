package com.plin.documents.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plin.documents.dto.ClientCreateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class ClientControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;
    private ClientCreateDTO clientCreateDto;

    @BeforeEach
    void setUp(){
        existingId = 1L;
        nonExistingId = 1000L;
        clientCreateDto = new ClientCreateDTO("Gustavo Pontes", "gustavo.pontes@gmail.com");
    }

    @Test
    public void getClientDocumentsShouldReturnOKWhenExistingId() throws Exception {
        mockMvc.perform(get("/clients/{id}/documents", existingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.creationDate").exists())
                .andExpect(jsonPath("$.documents").exists());
    }

    @Test
    public void getClientDocumentsShouldReturnNotFoundWhenNonExistingId() throws Exception {
        mockMvc.perform(get("/clients/{id}/documents", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAllClientsShouldReturnOK() throws Exception {
        mockMvc.perform(get("/clients")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].email").exists())
                .andExpect(jsonPath("$[0].creationDate").exists())
                .andExpect(jsonPath("$[0].documentAmount").exists());
    }

    @Test
    public void createClientShouldReturnOKWhenUniqueEmail() throws Exception {
        String json = objectMapper.writeValueAsString(clientCreateDto);
        mockMvc.perform(post("/clients")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void createClientShouldReturnConflictWhenDuplicateEmail() throws Exception {
        clientCreateDto.setEmail("jodias@email.com");
        String json = objectMapper.writeValueAsString(clientCreateDto);
        mockMvc.perform(post("/clients")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void updateClientShouldReturnOKWhenUniqueEmailAndExistingId() throws Exception {
        String json = objectMapper.writeValueAsString(clientCreateDto);
        mockMvc.perform(put("/clients/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void updateClientShouldReturnNotFoundWhenNonExistingId() throws Exception {
        String json = objectMapper.writeValueAsString(clientCreateDto);
        mockMvc.perform(put("/clients/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateClientShouldReturnConflictWhenDuplicateEmail() throws Exception {
        clientCreateDto.setEmail("jodias@email.com");
        String json = objectMapper.writeValueAsString(clientCreateDto);
        mockMvc.perform(put("/clients/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void deleteClientShouldReturnNoContentWhenExistingId() throws Exception {
        mockMvc.perform(delete("/clients/{id}", existingId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteClientShouldReturnNotFoundWhenNonExistingId() throws Exception {
        mockMvc.perform(delete("/clients/{id}", nonExistingId))
                .andExpect(status().isNotFound());
    }
}
