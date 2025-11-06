package com.plin.documents.service;

import com.plin.documents.dto.DocumentContentDTO;
import com.plin.documents.dto.DocumentDTO;
import com.plin.documents.dto.DocumentUrl;
import com.plin.documents.entity.Client;
import com.plin.documents.entity.Document;
import com.plin.documents.exception.DocumentException;
import com.plin.documents.exception.ResourceNotFoundException;
import com.plin.documents.repository.ClientRepository;
import com.plin.documents.repository.DocumentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class DocumentServiceTests {

    @InjectMocks
    private DocumentService service;

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private MultipartFile file;

    @Mock
    private DocumentExtractorService extractorService;

    private Long existingId;
    private Long nonExistingId;
    private Document document;
    private DocumentDTO documentDto;
    private DocumentContentDTO documentContentDto;
    private Client client;
    private DocumentUrl documentUrl;

    @BeforeEach
    void setUp(){
        existingId = 1L;
        nonExistingId = 1000L;
        client = new Client(existingId, "João", "joao@email.com", LocalDate.now());
        document = new Document(existingId, "Título", "conteudo teste".getBytes(), LocalDate.now(), client);
        documentDto = new DocumentDTO(document.getId(), document.getTitle(), document.getCreationDate());
        documentContentDto = new DocumentContentDTO(document.getTitle(), new ByteArrayResource(document.getContent()));
        documentUrl = new DocumentUrl("https://www.google.com.br");
        Mockito.when(documentRepository.save(any())).thenReturn(document);
        Mockito.when(documentRepository.findById(existingId)).thenReturn(Optional.of(document));
        Mockito.when(documentRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        Mockito.when(documentRepository.findAllByClientId(any())).thenReturn(List.of(documentDto));
        Mockito.when(clientRepository.findById(existingId)).thenReturn(Optional.of(client));
        Mockito.when(clientRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        Mockito.when(file.getOriginalFilename()).thenReturn(document.getTitle());
        Mockito.when(extractorService.savePageAsPdf(any())).thenReturn(document);
    }

    @Test
    public void getDocumentsByClientIdShouldReturnListOfDocumentDTO(){
        List<DocumentDTO> documents = service.getDocumentsByClientId(existingId);
        Assertions.assertNotNull(documents);
    }

    @Test
    public void downloadDocumentShouldReturnDocumentContentDTOWhenExistingId(){
        DocumentContentDTO documentContent = service.downloadDocument(existingId);
        Assertions.assertEquals(document.getTitle(), documentContent.getTitle());
        Assertions.assertEquals(document.getContent(), documentContent.getContent().getByteArray());
    }

    @Test
    public void downloadDocumentShouldThrowResourceNotFoundExceptionWhenNonExistingId(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.downloadDocument(nonExistingId));
    }

    @Test
    public void createDocumentFromUploadShouldReturnDocumentDTOWhenExistingClient() throws IOException {
        Mockito.when(file.getBytes()).thenReturn(document.getContent());
        DocumentDTO documentDTO = service.createDocumentFromUpload(existingId, file);
        Assertions.assertEquals(document.getId(), documentDTO.getId());
        Assertions.assertEquals(document.getTitle(), documentDTO.getTitle());
        Assertions.assertEquals(document.getCreationDate(), documentDTO.getCreationDate());
    }

    @Test
    public void createDocumentFromUploadShouldThrowResourceNotFoundExceptionWhenNonExistingClient() throws IOException {
        Mockito.when(file.getBytes()).thenReturn(document.getContent());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.createDocumentFromUpload(nonExistingId, file));
    }

    @Test
    public void createDocumentFromUploadShouldThrowDocumentExceptionWhenDocumentReadException() throws IOException {
        Mockito.when(file.getBytes()).thenThrow(IOException.class);
        Assertions.assertThrows(DocumentException.class, () -> service.createDocumentFromUpload(existingId, file));
    }

    @Test
    public void createDocumentFromUrlShouldReturnDocumentDTOWhenExistingClient() throws IOException {
        DocumentDTO documentDTO = service.createDocumentFromUrl(existingId, documentUrl);
        Assertions.assertEquals(document.getId(), documentDTO.getId());
        Assertions.assertEquals(document.getTitle(), documentDTO.getTitle());
        Assertions.assertEquals(document.getCreationDate(), documentDTO.getCreationDate());
    }

    @Test
    public void createDocumentFromUrlShouldThrowResourceNotFoundExceptionWhenNonExistingClient() throws IOException {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.createDocumentFromUrl(nonExistingId, documentUrl));
    }
}
