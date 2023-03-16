package com.bp.appbanco.repository.account;

import com.bp.appbanco.errors.EntityNotFoundException;
import com.bp.appbanco.model.account.Account;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountDaoImpl implements AccountDao {

  private final EntityManager entityManager;
  @Override
  public Account getAccountIfExists(Long accountId) {
    TypedQuery<Account> query = entityManager.createQuery(
      "select a from Account a where a.id = :id and a.status = com.bp.appbanco.model.emuns.Status.ACTIVE", Account.class);
    query.setParameter("id", accountId);
    return query.getResultStream().findFirst().orElseThrow(() -> new EntityNotFoundException("Account not found"));
  }
}
