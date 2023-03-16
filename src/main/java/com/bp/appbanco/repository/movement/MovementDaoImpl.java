package com.bp.appbanco.repository.movement;

import com.bp.appbanco.errors.EntityNotFoundException;
import com.bp.appbanco.model.movements.Movement;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MovementDaoImpl implements MovementDao {
  private final EntityManager entityManager;
  
  public Movement getMovementIfExists(Long id) {
    TypedQuery<Movement> query = entityManager.createQuery("SELECT m FROM Movement m WHERE m.movementId=:id", Movement.class)
      .setParameter("id", id);
    return query.getResultStream().findFirst().orElseThrow(() -> new EntityNotFoundException("Client not found"));
  }
}
