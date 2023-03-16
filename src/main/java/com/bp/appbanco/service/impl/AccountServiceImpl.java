package com.bp.appbanco.service.impl;

import com.bp.appbanco.model.account.Account;
import com.bp.appbanco.model.account.CurrentAccount;
import com.bp.appbanco.model.account.SavingAccounts;
import com.bp.appbanco.model.account.mapper.AccountMapper;
import com.bp.appbanco.model.emuns.Status;
import com.bp.appbanco.model.person.Client;
import com.bp.appbanco.repository.account.AccountRepository;
import com.bp.appbanco.repository.client.ClientRepository;
import com.bp.appbanco.service.AccountService;
import com.bp.appbanco.service.dto.request.RegisterAccountRequest;
import com.bp.appbanco.service.dto.response.AccountResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.bp.appbanco.service.utils.Utils.returnFirstNotNull;

@Service
@RequiredArgsConstructor
@Log4j2
public class AccountServiceImpl implements AccountService {
  private final AccountRepository accountRepository;
  private final AccountMapper accountMapper;
  private final ClientRepository clientRepository;

  @Override
  public AccountResponse registerCurrentAccount(RegisterAccountRequest createAccountRequest) {
    log.info("Registering current account for client: " + createAccountRequest.getClientId());
    Client client = clientRepository.getClientIfExists(createAccountRequest.getClientId());
    log.info("Client found: " + client.getName());
    CurrentAccount account =  accountMapper.accountDtoToAccount(createAccountRequest, CurrentAccount.class);
    populateAccount(account, client);
    account = accountRepository.save(account);
    log.info("Account created: " + account.getAccountId());
    return accountMapper.accountToAccountDto(account);
  }

  @Override
  public AccountResponse registerSavingsAccount(RegisterAccountRequest createAccountRequest) {
    log.info("Registering savings account for client: " + createAccountRequest.getClientId());
    Client client = clientRepository.getClientIfExists(createAccountRequest.getClientId());
    log.info("Client found: " + client.getName());
    SavingAccounts account =  accountMapper.accountDtoToAccount(createAccountRequest, SavingAccounts.class);
    populateAccount(account, client);
    account = accountRepository.save(account);
    log.info("Account created: " + account.getAccountId());
    return accountMapper.accountToAccountDto(account);
  }

  private void populateAccount(Account account, Client client) {
    account.setStatus(Status.ACTIVE);
    account.setTodayTotalWithdrawal(BigDecimal.ZERO);
    account.setClient(client);
    account.setCurrentBalance(account.getInitialBalance());
  }

  @Override
  public AccountResponse updateAccount(Long accountId, RegisterAccountRequest createAccountRequest) {
    log.info("Updating account: " + accountId);
    Account account = accountRepository.getAccountIfExists(accountId);
    log.info("Account found: " + account.getAccountNumber());
    account.setAccountNumber(returnFirstNotNull(createAccountRequest.getAccountNumber(), account.getAccountNumber()));
    account = accountRepository.save(account);
    return accountMapper.accountToAccountDto(account);
  }

  @Override
  public boolean deleteAccount(Long accountId){
    log.info("Deleting account: " + accountId); 
    Account account = accountRepository.getAccountIfExists(accountId);
    log.info("Account found: " + account.getAccountNumber());
    account.setStatus(Status.INACTIVE);
    accountRepository.save(account);
    log.info("Account deleted: " + account.getAccountNumber());
    return true;
  }

  @Override
  public boolean deleteAllClientAccounts(Long clientId) {
    log.info("Deleting all accounts for client: " + clientId);
    Client client = clientRepository.getClientIfExists(clientId);
    log.info("Client found: " + client.getName());
    Set<Account> accounts = client.getAccounts().stream()
      .peek(account -> account.setStatus(Status.INACTIVE))
      .collect(Collectors.toSet());
    accountRepository.saveAll(accounts);
    log.info("All accounts deleted for client: " + client.getName());
    return true;
  }

  @Override
  public List<AccountResponse> listAccounts(Long clientId) {
    log.info("Listing all accounts for client: " + clientId);
    return accountRepository.findByClient_PersonId(clientId).stream()
            .map(accountMapper::accountToAccountDto)
            .toList();
  }

  @Override
  public AccountResponse getAccount(Long accountId) {
    log.info("Getting account: " + accountId);
    return accountMapper.accountToAccountDto(accountRepository.getAccountIfExists(accountId));
  }
}
