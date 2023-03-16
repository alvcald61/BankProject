package com.bp.appbanco.service;

import com.bp.appbanco.service.dto.request.RegisterMovementRequest;
import com.bp.appbanco.service.dto.response.MovementResponse;

import java.util.List;

public interface MovementService {
  MovementResponse registerMovement(RegisterMovementRequest request);

  MovementResponse revertMovement(Long id);

  //  MovementResponse updateMovement(RegisterMovementRequest request);
//  MovementResponse revertMovement(Long id);
  MovementResponse getMovement(Long id);
  List<MovementResponse> getMovements(Long accountId);
}
