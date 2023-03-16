package com.bp.appbanco.service.impl;

import com.bp.appbanco.repository.report.ReportDao;
import com.bp.appbanco.repository.report.dto.ReportRawData;
import com.bp.appbanco.service.ReportService;
import com.bp.appbanco.service.dto.response.ReportResponse;
import com.bp.appbanco.service.dto.response.ReportResponseItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReportServiceImpl implements ReportService {

  private final ReportDao reportDao;
  
  @Override
  public ReportResponse getReport(Long userId, Date startDate, Date endDate) {
    endDate = getEndOfDay(endDate);
    startDate = getStartOfDay(startDate);
    log.info("Getting report for user: " + userId);
    log.info("Start Time: {} - End Time: {}", startDate, endDate);
    List<ReportRawData> reportRawData = reportDao.getReport(userId, startDate, endDate);
    List<ReportResponseItem> items = reportRawData.stream().sorted(Comparator.comparing(ReportRawData::getDate)).map(ReportResponseItem::new).collect(Collectors.toList());
    log.info("Report items: " + items.size());
    ReportResponse reportResponse = new ReportResponse(items);
    reportResponse.calculateTotals();
    return reportResponse;
  }

  private Date getEndOfDay(Date endDate) {
    LocalDate localDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.MAX);
    return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
  }

  private Date getStartOfDay(Date startDate) {
    LocalDate localDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.MIN);
    return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
  }
}
