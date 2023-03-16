package com.bp.appbanco.scheduler;

import com.bp.appbanco.model.account.Account;
import com.bp.appbanco.repository.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Log4j2
@RequiredArgsConstructor
public class AccountScheduler {
  
  private final AccountRepository accountRepository;
  
  @Scheduled(cron = "0 0 0 * * *")
  public void scheduleTaskWithCronExpression() {
    log.info("Resenting account withdrawal for the day");
    List<Account> accounts = accountRepository.findAll();
    accounts.forEach(account -> account.setTodayTotalWithdrawal(BigDecimal.ZERO));
    accountRepository.saveAll(accounts);
    log.info("Successfully reset account withdrawal for the day");
  }



}
