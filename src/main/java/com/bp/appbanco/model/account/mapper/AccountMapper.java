package com.bp.appbanco.model.account.mapper;

import com.bp.appbanco.model.account.Account;
import com.bp.appbanco.model.account.SavingAccounts;
import com.bp.appbanco.service.dto.request.RegisterAccountRequest;
import com.bp.appbanco.service.dto.response.AccountResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountMapper {
  private final ModelMapper modelMapper ;
  
  public AccountResponse accountToAccountDto(Account account) {
    var result =  modelMapper.map(account, AccountResponse.class);
    result.setClient(account.getClient().getName());
    result.setAccountType(account instanceof SavingAccounts ? "Saving Account" : "Current Account");
    return result;
  }
  
  public<T> T accountDtoToAccount(RegisterAccountRequest request, Class<T> accountClass) {
    return modelMapper.map(request, accountClass);
  }
  
  
}
