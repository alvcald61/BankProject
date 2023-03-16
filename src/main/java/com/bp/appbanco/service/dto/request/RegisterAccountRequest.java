package com.bp.appbanco.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RegisterAccountRequest {
  @NotBlank(message = "Account number is required")
  @Length(min= 5, message = "Account number must be at least 5 characters long")
  private String accountNumber;
  @PositiveOrZero
  private String initialBalance;
  @Positive(message = "Client id must be positive")
  @NotNull(message = "Client id is required")
  private Long clientId;
}
