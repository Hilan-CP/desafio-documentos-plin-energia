package com.plin.documents.service;

import com.plin.documents.dto.ClientCreateDTO;
import com.plin.documents.dto.ClientDTO;
import com.plin.documents.dto.ClientWithDocumentsDTO;
import com.plin.documents.entity.Client;
import com.plin.documents.exception.ResourceNotFoundException;
import com.plin.documents.mapper.ClientMapper;
import com.plin.documents.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional(readOnly = true)
    public ClientWithDocumentsDTO getClientDocuments(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        return ClientMapper.toClientWithDocumentsDto(client);
    }

    @Transactional(readOnly = true)
    public List<ClientDTO> getAllClients(){
        List<ClientDTO> clients = clientRepository.findAllClients();
        return clients;
    }

    @Transactional
    public ClientDTO createClient(ClientCreateDTO dto){
        Client entity = new Client();
        entity = ClientMapper.toEntity(dto, entity);
        entity.setCreationDate(LocalDate.now());
        entity = clientRepository.save(entity);
        return ClientMapper.toClientDto(entity);
    }

    @Transactional
    public ClientDTO updateClient(Long id, ClientCreateDTO dto){
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
