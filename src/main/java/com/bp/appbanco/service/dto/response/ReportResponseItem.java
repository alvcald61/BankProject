package com.bp.appbanco.service.dto.response;

import com.bp.appbanco.model.emuns.Status;
import com.bp.appbanco.repository.report.dto.ReportRawData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static com.bp.appbanco.service.utils.Utils.formatDateToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponseItem {
  private String date;
  private String client;
  private String accountNumber;
  private String accountType;
  private BigDecimal initialBalance;
  private boolean status;
  private BigDecimal amount;
  private BigDecimal finalBalance;
  
  public ReportResponseItem(ReportRawData data){
    this.date = formatDateToString(data.getDate()); 
    this.client = data.getClient();
    this.accountNumber = data.getAccountNumber();
    this.accountType = data.getAccountType();
    this.initialBalance = data.getInitialBalance();
    this.status = data.getStatus().equals(Status.ACTIVE);
    this.amount = data.getAmount();
    this.finalBalance = data.getInitialBalance().add(data.getAmount()); 
  }
}
