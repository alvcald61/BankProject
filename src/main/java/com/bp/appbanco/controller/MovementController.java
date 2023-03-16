package com.bp.appbanco.controller;

import com.bp.appbanco.service.MovementService;
import com.bp.appbanco.service.dto.request.RegisterMovementRequest;
import com.bp.appbanco.service.dto.response.MovementResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/movement")
@RequiredArgsConstructor
public class MovementController {
  
  private final MovementService movementService;
  
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public MovementResponse registerMovement(@Valid @RequestBody RegisterMovementRequest request) {
    return movementService.registerMovement(request);
  }
  
  @PostMapping("/{id}/revert")
  @ResponseStatus(HttpStatus.OK)
  public MovementResponse revertMovement(@PathVariable Long id) {
    return movementService.revertMovement(id);
  }
  
  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public MovementResponse getMovement(@PathVariable Long id) {
    return movementService.getMovement(id);
  }
  
  @GetMapping("/account/{accountId}")
  @ResponseStatus(HttpStatus.OK)
  public List<MovementResponse> getMovements(@PathVariable Long accountId) {
    return movementService.getMovements(accountId);
  }
}
