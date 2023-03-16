package com.bp.appbanco.repository.movement;

import com.bp.appbanco.model.movements.Movement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovementRepository extends JpaRepository<Movement, Long>, MovementDao{
}
