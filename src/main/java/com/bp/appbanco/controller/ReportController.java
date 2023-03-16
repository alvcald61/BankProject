package com.bp.appbanco.controller;

import com.bp.appbanco.service.ReportService;
import com.bp.appbanco.service.dto.response.ReportResponse;
import com.bp.appbanco.service.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
public class ReportController {
  private final ReportService reportService;
  
  
  @GetMapping("/client/{clientId}")
  @ResponseStatus(HttpStatus.OK)
  //date format dd/MM/YYYY-dd/MM/YYYY
  public ReportResponse generateClientReport(@PathVariable Long clientId, @RequestParam String date){
    String[] dates = date.split("-");
    Date startDate = Utils.parseStringToDate(dates[0]);
    Date endDate = Utils.parseStringToDate(dates[1]);
    return reportService.getReport(clientId, startDate, endDate);
  }
}
