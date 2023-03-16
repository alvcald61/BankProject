package com.bp.appbanco.model.movements.mapper;

import com.bp.appbanco.model.emuns.MovementType;
import com.bp.appbanco.model.movements.Movement;
import com.bp.appbanco.service.dto.request.RegisterMovementRequest;
import com.bp.appbanco.service.dto.response.MovementResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class MovementMapper {
  private final ModelMapper modelMapper = new ModelMapper();
  
  public Movement movementDtoToMovement(RegisterMovementRequest request) {
    return Movement.builder()
      .amount(request.getAmount())
      .movementType(request.getAmount().compareTo(BigDecimal.ZERO) < 0 ? MovementType.WITHDRAWAL : MovementType.DEPOSIT)
      .build();
  }
  
  public MovementResponse movementToMovementDto(Movement movement) {
    return modelMapper.map(movement, MovementResponse.class);
  }
  
  public RegisterMovementRequest movementToMovementRequest(Movement movement) {
    RegisterMovementRequest request = modelMapper.map(movement, RegisterMovementRequest.class);
    request.setAccountId(movement.getAccount().getAccountId());
    request.setAmount(movement.getAmount());
    return request;
  }
  
  
}
