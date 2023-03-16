package com.bp.appbanco.controller;

import com.bp.appbanco.service.AccountService;
import com.bp.appbanco.service.dto.request.RegisterAccountRequest;
import com.bp.appbanco.service.dto.response.AccountResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {
  private final AccountService accountService;
  
  @GetMapping("/client/{clientId}")
  @ResponseStatus(HttpStatus.OK)
  public List<AccountResponse> getAccounts(@PathVariable Long clientId) {
    return accountService.listAccounts(clientId);
  }
  
  @PostMapping("/current")
  @ResponseStatus(HttpStatus.CREATED)
  public AccountResponse createCurrentAccount(@Valid @RequestBody RegisterAccountRequest createAccountRequest) {
    return accountService.registerCurrentAccount(createAccountRequest);
  }
  
  @PostMapping("/savings")
  @ResponseStatus(HttpStatus.CREATED)
  public AccountResponse createSavingsAccount(@Valid @RequestBody RegisterAccountRequest createAccountRequest) {
    return accountService.registerSavingsAccount(createAccountRequest);
  }
  
  @PatchMapping("/{accountId}")
  @ResponseStatus(HttpStatus.OK)
  public AccountResponse patchAccount(@PathVariable Long accountId, @RequestBody RegisterAccountRequest createAccountRequest) {
    return accountService.updateAccount(accountId, createAccountRequest);
  }
  
  @DeleteMapping("/{accountId}")
  @ResponseStatus(HttpStatus.OK)
  public boolean deleteAccount(@PathVariable Long accountId) {
    return accountService.deleteAccount(accountId);
  }
  
  @DeleteMapping("/client/{clientId}")
  @ResponseStatus(HttpStatus.OK)
  public boolean deleteAllClientAccounts(@PathVariable Long clientId) {
    return accountService.deleteAllClientAccounts(clientId);
  }
  
  @GetMapping("{accountId}")
  @ResponseStatus(HttpStatus.OK)
  public AccountResponse getAccount(@PathVariable Long accountId) {
    return accountService.getAccount(accountId);
  }
}
