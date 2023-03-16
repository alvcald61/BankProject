package com.bp.appbanco.service.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovementResponse {
  private Long movementId;
  private String accountNumber;
  private String amount;
  private String movementType;
  private String movementDate;
  private String initialBalance; 
  
}
