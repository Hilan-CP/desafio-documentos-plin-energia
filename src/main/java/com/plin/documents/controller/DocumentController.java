package com.plin.documents.controller;

import com.plin.documents.dto.DocumentContentDTO;
import com.plin.documents.dto.DocumentDTO;
import com.plin.documents.dto.DocumentUrl;
import com.plin.documents.service.DocumentService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentController {
    private final DocumentService service;

    public DocumentController(DocumentService service) {
        this.service = service;
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<DocumentDTO>> getDocumentsByClientId(@PathVariable Long clientId){
        List<DocumentDTO> documents = service.getDocumentsByClientId(clientId);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long id){
        DocumentContentDTO document = service.downloadDocument(id);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment;filename=" + document.getTitle())
                .contentType(MediaType.APPLICATION_PDF)
                .body(document.getContent());
    }

    @PostMapping("/upload")
    public ResponseEntity<DocumentDTO> createDocumentFromUpload(@RequestHeader("X-Client-Id") Long clientId, @RequestBody MultipartFile file) {
        DocumentDTO document = service.createDocumentFromUpload(clientId, file);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").build(document.getId());
        return ResponseEntity.created(uri).body(document);
    }

    @PostMapping("/url")
    public ResponseEntity<DocumentDTO> createDocumentFromUrl(@RequestHeader("X-Client-Id") Long clientId, @RequestBody DocumentUrl documentUrl){
        DocumentDTO document = service.createDocumentFromUrl(clientId, documentUrl);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").build(document.getId());
        return ResponseEntity.created(uri).body(document);
    }
}
