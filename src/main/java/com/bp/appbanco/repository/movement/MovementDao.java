package com.bp.appbanco.repository.movement;

import com.bp.appbanco.model.movements.Movement;

public interface MovementDao {
  Movement getMovementIfExists(Long id);
}
