package com.plin.documents.service;

import com.plin.documents.dto.ClientCreateDTO;
import com.plin.documents.dto.ClientDTO;
import com.plin.documents.dto.ClientWithDocumentsDTO;
import com.plin.documents.dto.DocumentDTO;
import com.plin.documents.entity.Client;
import com.plin.documents.entity.Document;
import com.plin.documents.exception.ResourceNotFoundException;
import com.plin.documents.exception.UniqueFieldException;
import com.plin.documents.repository.ClientRepository;
import com.plin.documents.repository.DocumentRepository;
import com.plin.documents.validation.ClientValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class ClientServiceTests {

    @InjectMocks
    private ClientService service;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private ClientValidator validator;

    private Long existingId;
    private Long nonExistingId;
    private Client client;
    private Client savedClient;
    private ClientDTO clientDto;
    private ClientCreateDTO clientCreateDto;
    private Document document;
    private DocumentDTO documentDto;

    // inicializa as variáveis e configura os mocks antes de cada teste
    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 1000L;
        client = new Client(existingId, "João", "joao@email.com", LocalDate.now());
        clientDto = new ClientDTO(null, "João", "joao@email.com", LocalDate.now(), 1L);
        clientCreateDto = new ClientCreateDTO("Renato", "renato@email.com");
        savedClient = new Client(2L, clientCreateDto.getName(), clientCreateDto.getEmail(), LocalDate.now());
        document = new Document(1L, "Doc Teste", "conteudo exemplo".getBytes(), LocalDate.now(), client);
        client.getDocuments().add(document);
        documentDto = new DocumentDTO(document.getId(), document.getTitle(), document.getCreationDate());
        Mockito.when(clientRepository.findById(existingId)).thenReturn(Optional.of(client));
        Mockito.when(clientRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        Mockito.when(clientRepository.findAllClients()).thenReturn(List.of(clientDto));
        Mockito.when(clientRepository.save(any())).thenReturn(savedClient);
        Mockito.when(clientRepository.existsById(existingId)).thenReturn(true);
        Mockito.when(clientRepository.existsById(nonExistingId)).thenReturn(false);
        Mockito.when(clientRepository.existsByEmailAndNotId(client.getEmail(), 0L)).thenReturn(true);
        Mockito.when(clientRepository.existsByEmailAndNotId(client.getEmail(), savedClient.getId())).thenReturn(false);
        Mockito.doNothing().when(validator).validate(any());
        Mockito.when(documentRepository.findAllByClientId(existingId)).thenReturn(List.of(documentDto));
    }

    @Test
    public void getClientDocumentsShouldReturnClientWithDocumentsDTOWhenExistingId() {
        ClientWithDocumentsDTO clientWithDocuments = service.getClientDocuments(existingId);
        Assertions.assertEquals(client.getId(), clientWithDocuments.getId());
        Assertions.assertEquals(client.getName(), clientWithDocuments.getName());
        Assertions.assertEquals(client.getEmail(), clientWithDocuments.getEmail());
        Assertions.assertEquals(client.getCreationDate(), clientWithDocuments.getCreationDate());
        Assertions.assertEquals(client.getDocuments().size(), clientWithDocuments.getDocuments().size());
    }

    @Test
    public void getClientDocumentsShouldThrowResourceNotFoundExceptionWhenNonExistingId() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.getClientDocuments(nonExistingId));
    }

    @Test
    public void getClientDocumentsShouldReturnListOfClientDTO() {
        List<ClientDTO> clients = service.getAllClients();
        Assertions.assertFalse(clients.isEmpty());
    }

    @Test
    public void createClientShouldReturnClientDTOWhenUniqueEmail() {
        ClientDTO newClient = service.createClient(clientCreateDto);
        Assertions.assertNotNull(newClient.getId());
        Assertions.assertEquals(clientCreateDto.getName(), newClient.getName());
        Assertions.assertEquals(clientCreateDto.getEmail(), newClient.getEmail());
        Assertions.assertNotNull(newClient.getCreationDate());
    }

    @Test
    public void createClientShouldThrowUniqueFieldExceptionWhenDuplicateEmail() {
        clientCreateDto.setEmail(client.getEmail());
        Assertions.assertThrows(UniqueFieldException.class, () -> service.createClient(clientCreateDto));
    }

    @Test
    public void updateClientShouldReturnClientDTOWhenExistingId() {
        ClientDTO updatedClient = service.updateClient(existingId, clientCreateDto);
        Assertions.assertNotNull(updatedClient.getId());
        Assertions.assertEquals(clientCreateDto.getName(), updatedClient.getName());
        Assertions.assertEquals(clientCreateDto.getEmail(), updatedClient.getEmail());
        Assertions.assertNotNull(updatedClient.getCreationDate());
    }

    @Test
    public void updateClientShouldThrowResourceNotFoundExceptionWhenNonExistingId() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.updateClient(nonExistingId, clientCreateDto));
    }

    @Test
    public void updateClientShouldThrowUniqueFieldExceptionWhenDuplicateEmail() {
        clientCreateDto.setEmail(client.getEmail());
        Assertions.assertThrows(UniqueFieldException.class, () -> service.createClient(clientCreateDto));
    }

    @Test
    public void deleteClientShouldDoNothingWhenExistingId() {
        Assertions.assertDoesNotThrow(() -> service.deleteClient(existingId));
    }

    @Test
    public void deleteClientShouldThrowResourceNotFoundExceptionWhenNonExistingId() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.deleteClient(nonExistingId));
    }
}
