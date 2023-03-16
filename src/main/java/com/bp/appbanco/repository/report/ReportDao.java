package com.bp.appbanco.repository.report;

import com.bp.appbanco.repository.report.dto.ReportRawData;

import java.util.Date;
import java.util.List;

public interface ReportDao {
  List<ReportRawData> getReport(Long userId, Date startDate, Date endDate);
}
