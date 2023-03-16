package com.bp.appbanco.service;

import com.bp.appbanco.service.dto.request.RegisterAccountRequest;
import com.bp.appbanco.service.dto.response.AccountResponse;

import java.util.List;

public interface AccountService {
  AccountResponse registerCurrentAccount(RegisterAccountRequest createAccountRequest);
  AccountResponse registerSavingsAccount(RegisterAccountRequest createAccountRequest);
  AccountResponse updateAccount(Long accountId, RegisterAccountRequest createAccountRequest);
  boolean deleteAccount(Long accountId);
  boolean deleteAllClientAccounts(Long clientId);
  List<AccountResponse> listAccounts(Long clientId);

  AccountResponse getAccount(Long accountId);
}
