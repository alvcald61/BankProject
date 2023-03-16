package com.bp.appbanco.repository.report.dto;

import com.bp.appbanco.model.emuns.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportRawData {
  private Date date;
  private String client;
  private String accountNumber;
  private BigDecimal initialBalance;
  private Status status;
  private BigDecimal amount;
  private String accountType;
}
