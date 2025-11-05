package com.plin.documents.service;

import com.plin.documents.dto.ClientCreateDTO;
import com.plin.documents.dto.ClientDTO;
import com.plin.documents.dto.ClientWithDocumentsDTO;
import com.plin.documents.dto.DocumentDTO;
import com.plin.documents.entity.Client;
import com.plin.documents.exception.ResourceNotFoundException;
import com.plin.documents.exception.UniqueFieldException;
import com.plin.documents.mapper.ClientMapper;
import com.plin.documents.repository.ClientRepository;
import com.plin.documents.repository.DocumentRepository;
import com.plin.documents.validation.ClientValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final DocumentRepository documentRepository;
    private final ClientValidator validator;

    public ClientService(ClientRepository clientRepository, DocumentRepository documentRepository, ClientValidator validator) {
        this.clientRepository = clientRepository;
        this.documentRepository = documentRepository;
        this.validator = validator;
    }

    @Transactional(readOnly = true)
    public ClientWithDocumentsDTO getClientDocuments(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        List<DocumentDTO> documentDtoList = documentRepository.findAllByClientId(id);
        return ClientMapper.toClientWithDocumentsDto(client, documentDtoList);
    }

    @Transactional(readOnly = true)
    public List<ClientDTO> getAllClients(){
        List<ClientDTO> clients = clientRepository.findAllClients();
        return clients;
    }

    @Transactional
    public ClientDTO createClient(ClientCreateDTO dto){
        validator.validate(dto);
        if(clientRepository.existsByEmailAndNotId(dto.getEmail(), 0L)){
            throw new UniqueFieldException("Email já cadastrado para outro cliente");
        }
        Client entity = new Client();
        entity = ClientMapper.toEntity(dto, entity);
        entity.setCreationDate(LocalDate.now());
        entity = clientRepository.save(entity);
        return ClientMapper.toClientDto(entity);
    }

    @Transactional
    public ClientDTO updateClient(Long id, ClientCreateDTO dto){
        validator.validate(dto);
        if(clientRepository.existsByEmailAndNotId(dto.getEmail(), id)){
            throw new UniqueFieldException("Email já cadastrado para outro cliente");
        }
        Client entity = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        entity = ClientMapper.toEntity(dto, entity);
        entity = clientRepository.save(entity);
        return ClientMapper.toClientDto(entity);
    }

    @Transactional
    public void deleteClient(Long id){
        if(!clientRepository.existsById(id)){
            throw new ResourceNotFoundException("Cliente não encontrado");
        }
        clientRepository.deleteById(id);
    }
}
