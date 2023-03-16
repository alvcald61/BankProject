package com.bp.appbanco.service.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RegisterMovementRequest {
  @NotNull(message = "Amount is required")
  private BigDecimal amount;
  @NotNull(message = "AccountId is required") 
  @Positive(message = "AccountId must be positive")
  private Long accountId;
}
