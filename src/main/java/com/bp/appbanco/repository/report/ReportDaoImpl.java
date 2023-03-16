package com.bp.appbanco.repository.report;

import com.bp.appbanco.repository.report.dto.ReportRawData;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReportDaoImpl implements ReportDao {
  
  private final EntityManager entityManager;
  
  @Override
  public List<ReportRawData> getReport(Long userId, Date startDate, Date endDate) {
    String sql = """
      SELECT new com.bp.appbanco.repository.report.dto.ReportRawData
      (m.movementDate, c.name, a.accountNumber, m.initialBalance, a.status, m.amount, "Ahorro")
      FROM Client c
      INNER JOIN Account a ON a.client.personId = c.personId
      INNER JOIN SavingAccounts s ON s.accountId = a.accountId
      INNER JOIN Movement m ON m.account.accountId = a.accountId
      WHERE c.personId = :userId
      AND m.movementDate BETWEEN :startDate AND :endDate
      UNION ALL
      SELECT new com.bp.appbanco.repository.report.dto.ReportRawData
      (m.movementDate, c.name, a.accountNumber, m.initialBalance, a.status, m.amount, "Corriente")
      FROM Client c
      INNER JOIN Account a ON a.client.personId = c.personId
      INNER JOIN CurrentAccount ca ON ca.accountId = a.accountId
      INNER JOIN Movement m ON m.account.accountId = a.accountId
      WHERE c.personId = :userId
      AND m.movementDate BETWEEN :startDate AND :endDate
      """;
      TypedQuery<ReportRawData> query = entityManager.createQuery(sql, ReportRawData.class);
      query.setParameter("userId", userId); 
      query.setParameter("startDate", startDate);
      query.setParameter("endDate", endDate);
      return query.getResultList();
  }
}
