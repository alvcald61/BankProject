package com.bp.appbanco.repository.client;

import com.bp.appbanco.errors.EntityNotFoundException;
import com.bp.appbanco.model.person.Client;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ClientDaoImpl implements ClientDao{
  private final EntityManager entityManager;
  
  public Client getClientIfExists(Long id) {
    TypedQuery<Client> query = entityManager.createQuery(
      "SELECT c FROM Client c WHERE c.personId = :id and c.status = com.bp.appbanco.model.emuns.Status.ACTIVE", Client.class)
      .setParameter("id", id);
    return query.getResultStream().findFirst().orElseThrow(() -> new EntityNotFoundException("Client not found"));
  }
}
