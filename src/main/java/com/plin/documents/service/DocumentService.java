package com.plin.documents.service;

import com.plin.documents.dto.DocumentContentDTO;
import com.plin.documents.dto.DocumentDTO;
import com.plin.documents.dto.DocumentUrl;
import com.plin.documents.entity.Client;
import com.plin.documents.entity.Document;
import com.plin.documents.exception.DocumentException;
import com.plin.documents.exception.ResourceNotFoundException;
import com.plin.documents.mapper.DocumentMapper;
import com.plin.documents.repository.ClientRepository;
import com.plin.documents.repository.DocumentRepository;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final ClientRepository clientRepository;

    public DocumentService(DocumentRepository documentRepository, ClientRepository clientRepository) {
        this.documentRepository = documentRepository;
        this.clientRepository = clientRepository;
    }

    public DocumentDTO createDocumentFromUpload(Long clientId, MultipartFile file) {
        try {
            byte[] content = file.getBytes();
            String title = file.getOriginalFilename();
            return createDocument(clientId, title, content);
        } catch (IOException e) {
            throw new DocumentException("Erro inesperado ao ler documento enviado", e);
        }
    }

    public DocumentDTO createDocumentFromUrl(Long clientId, DocumentUrl url){
        throw new UnsupportedOperationException("Não implementado");
    }

    @Transactional
    private DocumentDTO createDocument(Long clientId, String title, byte[] content){
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        Document document = new Document();
        document.setTitle(title);
        document.setContent(content);
        document.setCreationDate(LocalDate.now());
        document.setClient(client);
        document = documentRepository.save(document);
        return DocumentMapper.toDto(document);
    }

    @Transactional(readOnly = true)
    public DocumentContentDTO downloadDocument(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Documento não encontrado"));
        DocumentContentDTO documentContent = new DocumentContentDTO(
                document.getTitle(),
                new ByteArrayResource(document.getContent()));
        return documentContent;
    }

    @Transactional(readOnly = true)
    public List<DocumentDTO> getDocumentsByClientId(Long clientId) {
        List<Document> documents = documentRepository.findByClientId(clientId);
        return documents.stream()
                .map(document -> DocumentMapper.toDto(document))
                .toList();
    }
}
