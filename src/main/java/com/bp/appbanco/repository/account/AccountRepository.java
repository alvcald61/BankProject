package com.bp.appbanco.repository.account;

import com.bp.appbanco.model.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.Set;

public interface AccountRepository extends JpaRepository<Account, Long>, AccountDao {
  @Query("select a from Account a where a.client.personId = ?1")
  Set<Account> findByClient_PersonId(@NonNull Long personId);
}
