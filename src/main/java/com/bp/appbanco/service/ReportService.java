package com.bp.appbanco.service;

import com.bp.appbanco.service.dto.response.ReportResponse;

import java.util.Date;

public interface ReportService {
  ReportResponse getReport(Long userId, Date startDate, Date endDate);  
}
