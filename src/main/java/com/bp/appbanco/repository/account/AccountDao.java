package com.bp.appbanco.repository.account;

import com.bp.appbanco.model.account.Account;

public interface AccountDao {
  Account getAccountIfExists(Long accountId);  
}
