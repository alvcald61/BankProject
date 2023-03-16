package com.bp.appbanco.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
  private Long accountId;
  private String accountNumber;
  private String status;
  private String initialBalance;
  private String currentBalance;
  private String client;
  private String accountType;
}
