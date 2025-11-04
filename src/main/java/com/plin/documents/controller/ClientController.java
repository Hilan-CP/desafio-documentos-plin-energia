package com.plin.documents.controller;

import com.plin.documents.dto.ClientCreateDTO;
import com.plin.documents.dto.ClientDTO;
import com.plin.documents.dto.ClientWithDocumentsDTO;
import com.plin.documents.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {
    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @GetMapping("/{id}/documents")
    public ResponseEntity<ClientWithDocumentsDTO> getClientDocuments(@PathVariable Long id){
        ClientWithDocumentsDTO clientWithDocuments = service.getClientDocuments(id);
        return ResponseEntity.ok(clientWithDocuments);
    }

    @GetMapping
    public ResponseEntity<List<ClientDTO>> getAllClients(){
        List<ClientDTO> clients = service.getAllClients();
        return ResponseEntity.ok(clients);
    }

    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@RequestBody ClientCreateDTO dto){
        ClientDTO client = service.createClient(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").build(client.getId());
        return ResponseEntity.created(uri).body(client);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable Long id, @RequestBody ClientCreateDTO dto){
        ClientDTO client = service.updateClient(id, dto);
        return ResponseEntity.ok(client);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ClientDTO> deleteClient(@PathVariable Long id){
        service.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}
