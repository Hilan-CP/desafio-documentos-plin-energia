package com.plin.documents.repository;

import com.plin.documents.dto.ClientDTO;
import com.plin.documents.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {

    // consulta JPQL que retorna uma projeção de ClientDTO
    @Query("""
            SELECT NEW com.plin.documents.dto.ClientDTO(
                client.id,
                client.name,
                client.email,
                client.creationDate,
                COUNT(documents))
            FROM Client client
            LEFT JOIN client.documents documents
            GROUP BY client.id
            ORDER BY client.id
            """)
    List<ClientDTO> findAllClients();
}
