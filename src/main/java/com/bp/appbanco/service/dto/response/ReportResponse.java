package com.bp.appbanco.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {
  private List<ReportResponseItem> items;
  private BigDecimal totalWithdraw;
  private BigDecimal totalDeposit;
  private int total;

  public ReportResponse(List<ReportResponseItem> items) {
    this.items = items;
    this.total = items.size();
  }

  public void calculateTotals() {
    this.totalWithdraw = items.stream().map(ReportResponseItem::getAmount).filter(amount ->  amount.compareTo(BigDecimal.ZERO) <= 0).reduce(BigDecimal.ZERO, BigDecimal::add);
    this.totalDeposit = items.stream().map(ReportResponseItem::getAmount).filter(amount ->  amount.compareTo(BigDecimal.ZERO) >= 0).reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
