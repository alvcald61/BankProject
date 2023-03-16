package com.bp.appbanco.service.impl;

import com.bp.appbanco.model.account.Account;
import com.bp.appbanco.model.movements.Movement;
import com.bp.appbanco.model.movements.mapper.MovementMapper;
import com.bp.appbanco.repository.account.AccountRepository;
import com.bp.appbanco.repository.movement.MovementRepository;
import com.bp.appbanco.service.MovementService;
import com.bp.appbanco.service.dto.request.RegisterMovementRequest;
import com.bp.appbanco.service.dto.response.MovementResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import static com.bp.appbanco.errors.ErrorManager.*;


@Service
@Log4j2
public class MovementServiceImpl implements MovementService {

  private final MovementRepository movementRepository;
  private final AccountRepository accountRepository;
  private final MovementMapper movementMapper;
  private final String MAX_WITHDRAWAL;

  public MovementServiceImpl(MovementRepository movementRepository, AccountRepository accountRepository, MovementMapper movementMapper, @Value("${app.max-withdrawal-per-day}") String MAX_WITHDRAWAL) {
    this.movementRepository = movementRepository;
    this.accountRepository = accountRepository;
    this.movementMapper = movementMapper;
    this.MAX_WITHDRAWAL = MAX_WITHDRAWAL;
  }

  @Transactional
  @Override
  public MovementResponse registerMovement(RegisterMovementRequest request) {
    log.info("Registering movement for account: " + request.getAccountId());
    Account account = accountRepository.getAccountIfExists(request.getAccountId());
    log.info("Account found: " + account.getAccountNumber());
    Movement movement = movementMapper.movementDtoToMovement(request);
    log.info("Movement created: " + movement);
    validateAccountBalance(account, movement);
    validateTodayWithdrawal(account, movement);
    movement.updateWithAccountInfo(account);
    validateTransferAmount(account, movement);
    account.transfer(movement);
    movement = movementRepository.save(movement);
    log.info("Movement saved: " + movement.getMovementId());
    accountRepository.save(account);
    log.info("Account today's withdrawal updated: " + account.getAccountId());
    return movementMapper.movementToMovementDto(movement);
  }

  @Override
  public MovementResponse revertMovement(Long id) {
    Movement movement = movementRepository.getMovementIfExists(id);
    Account account = movement.getAccount();
    Movement revertMovement = movement.revert();
    validateAccountBalance(account, revertMovement);
    revertMovement.updateWithAccountInfo(account);
    validateTransferAmount(account, revertMovement);
    account.revertTransfer(revertMovement);
    revertMovement = movementRepository.save(revertMovement);
    log.info("Movement saved: " + revertMovement.getMovementId());
    accountRepository.save(account);
    log.info("Account today's withdrawal updated: " + account.getAccountId());
    return movementMapper.movementToMovementDto(revertMovement);
  }




  private void validateTransferAmount(Account account, Movement movement) {
    if (movement.isWithdrawal() && !account.isValidTrasfer(movement)) {
      throw AMOUNT_GRATHER_THAN_BALANCE;
    }
    log.info("The amount to withdraw is less than the account balance");
  }

  private void validateTodayWithdrawal(Account account, Movement movement) {
    if (movement.isWithdrawal() && account.isTodayMaxWithdrawalExceeded(movement, new BigDecimal(MAX_WITHDRAWAL))) {
      throw MAX_WITHDRAWAL_EXCEEDED;
    }
    log.info("Maximum withdrawal not exceeded");
  }

  private void validateAccountBalance(Account account, Movement movement) {
    if (movement.isWithdrawal() && account.isBalanceZero()) {
      throw WITHDRAWAL_WHEN_BALANCE_IS_ZERO;
    }
    log.info("The account balance is not zero");
  }


  @Override
  public MovementResponse getMovement(Long id) {
    log.info("Getting movement: " + id);
    return movementMapper.movementToMovementDto(movementRepository.getMovementIfExists(id));
  }

  @Override
  public List<MovementResponse> getMovements(Long accountId) {
    log.info("Getting movements for account: " + accountId);
    Account account = accountRepository.getAccountIfExists(accountId);
    return account.getMovements().stream().sorted(Comparator.comparing(Movement::getMovementDate)).map(movementMapper::movementToMovementDto).toList();
  }
}
