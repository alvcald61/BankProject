package com.bp.appbanco.service.impl;

import com.bp.appbanco.errors.NumericErrorException;
import com.bp.appbanco.model.account.Account;
import com.bp.appbanco.model.account.SavingAccounts;
import com.bp.appbanco.model.emuns.MovementType;
import com.bp.appbanco.model.emuns.Status;
import com.bp.appbanco.model.movements.Movement;
import com.bp.appbanco.model.movements.mapper.MovementMapper;
import com.bp.appbanco.repository.account.AccountRepository;
import com.bp.appbanco.repository.movement.MovementRepository;
import com.bp.appbanco.service.dto.request.RegisterMovementRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashSet;

import static com.bp.appbanco.errors.ErrorManager.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;


class MovementServiceImplTest {
  public static final String MAX_WITHDRAWAL = "1000";
  MovementRepository movementRepository;
  AccountRepository accountRepository;
  MovementMapper movementMapper;
  MovementServiceImpl movementServiceImpl;

  public MovementServiceImplTest() {
    movementRepository = mock(MovementRepository.class);
    accountRepository = mock(AccountRepository.class);
    movementMapper = new MovementMapper();
    movementServiceImpl = new MovementServiceImpl(movementRepository, accountRepository, movementMapper, MAX_WITHDRAWAL);
  }

  public Account generateAccount() {
    SavingAccounts account = new SavingAccounts();
    account.setAccountId(1L);
    account.setCurrentBalance(BigDecimal.valueOf(1000));
    account.setInitialBalance(BigDecimal.valueOf(1000));
    account.setAccountNumber("123456789");
    account.setTodayTotalWithdrawal(BigDecimal.valueOf(0));
    account.setStatus(Status.ACTIVE);
    account.setMovements(new HashSet<>());
    return account;
  }


  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Nested
  @DisplayName("Testing function validateTransferAmount")
  class ValidateTransferAmount {

    private Method getValidateTransferAmount() throws NoSuchMethodException {
      Method method = MovementServiceImpl.class.getDeclaredMethod("validateTransferAmount", Account.class, Movement.class);
      method.setAccessible(true);
      return method;
    }

    @Test
    @DisplayName("Should throw exception when amount is greater than balance")
    void shouldThrowExceptionWhenAmountGreatherThanCurrentBalance() {
      //given
      SavingAccounts account = new SavingAccounts();
      account.setCurrentBalance(BigDecimal.valueOf(1000));
      Movement movement = new Movement();
      movement.setAmount(BigDecimal.valueOf(-1000.01));
      movement.setMovementType(MovementType.WITHDRAWAL);
      //when
      var error = assertThatThrownBy(() -> ReflectionUtils.invokeMethod(getValidateTransferAmount(), movementServiceImpl, account, movement));
      //then
      error.isInstanceOf(NumericErrorException.class).hasMessage(AMOUNT_GRATHER_THAN_BALANCE.getMessage());
    }

    @Test
    @DisplayName("Should not throw exception when amount is less than balance")
    void validateTransferAmount2() {
      //given
      SavingAccounts account = new SavingAccounts();
      account.setCurrentBalance(BigDecimal.valueOf(1000));
      Movement movement = new Movement();
      movement.setAmount(BigDecimal.valueOf(-1000));
      movement.setMovementType(MovementType.WITHDRAWAL);
      //when
      var code = assertThatCode(() -> ReflectionUtils.invokeMethod(getValidateTransferAmount(), movementServiceImpl, account, movement));
      //then
      code.doesNotThrowAnyException();
    }
  }

  @Nested
  @DisplayName("Testing function validateTodayWithdrawal")
  class ValidateTodayWithdrawal {
    private Method getValidateTodayWithdrawal() throws NoSuchMethodException {
      Method method = MovementServiceImpl.class.getDeclaredMethod("validateTodayWithdrawal", Account.class, Movement.class);
      method.setAccessible(true);
      return method;
    }

    @Test
    @DisplayName("Should throw exception when today's withdrawal is greater than the limit")
    void shouldThrowExceptionWhenTodayWithdrawalIsGreater() {
      //given
      SavingAccounts account = new SavingAccounts();
      account.setTodayTotalWithdrawal(BigDecimal.valueOf(500));
      Movement movement = new Movement();
      movement.setAmount(BigDecimal.valueOf(-1000.01));
      movement.setMovementType(MovementType.WITHDRAWAL);
      //when
      var error = assertThatThrownBy(() -> ReflectionUtils.invokeMethod(getValidateTodayWithdrawal(), movementServiceImpl, account, movement));
      //then
      error.isInstanceOf(NumericErrorException.class).hasMessage(MAX_WITHDRAWAL_EXCEEDED.getMessage());
    }

    @Test
    @DisplayName("Should not throw exception when today's withdrawal is less than the limit")
    void shouldNotThrowExceptionWhenTodayWithdrawalIsLess() {
      //given
      SavingAccounts account = new SavingAccounts();
      account.setTodayTotalWithdrawal(BigDecimal.valueOf(500));
      Movement movement = new Movement();
      movement.setAmount(BigDecimal.valueOf(-500));
      movement.setMovementType(MovementType.WITHDRAWAL);
      //when
      var code = assertThatCode(() -> ReflectionUtils.invokeMethod(getValidateTodayWithdrawal(), movementServiceImpl, account, movement));
      //then
      code.doesNotThrowAnyException();
    }
  }

  @Nested
  @DisplayName("Testing function validateTodayWithdrawal")
  class validateAccountBalance {
    private Method getValidateAccountBalance() throws NoSuchMethodException {
      Method method = MovementServiceImpl.class.getDeclaredMethod("validateAccountBalance", Account.class, Movement.class);
      method.setAccessible(true);
      return method;
    }

    @Test
    @DisplayName("Should throw exception if balance is 0 and movement is withdrawal")
    void shouldThrowExceptionWhenTodayWithdrawalIsGreater() {
      //given
      SavingAccounts account = new SavingAccounts();
      account.setCurrentBalance(BigDecimal.ZERO);
      Movement movement = new Movement();
      movement.setAmount(BigDecimal.valueOf(-1000));
      movement.setMovementType(MovementType.WITHDRAWAL);
      //when
      var error = assertThatThrownBy(() -> ReflectionUtils.invokeMethod(getValidateAccountBalance(), movementServiceImpl, account, movement));
      //then
      error.isInstanceOf(NumericErrorException.class).hasMessage(WITHDRAWAL_WHEN_BALANCE_IS_ZERO.getMessage());
    }

    @Test
    @DisplayName("Should not throw exception if balance is 0 and movement is withdrawal")
    void shouldNotThrowExceptionWhenTodayWithdrawalIsLess() {
      //given
      SavingAccounts account = new SavingAccounts();
      account.setCurrentBalance(BigDecimal.valueOf(1000));
      Movement movement = new Movement();
      movement.setAmount(BigDecimal.valueOf(-100));
      movement.setMovementType(MovementType.WITHDRAWAL);
      //when
      var code = assertThatCode(() -> ReflectionUtils.invokeMethod(getValidateAccountBalance(), movementServiceImpl, account, movement));
      //then
      code.doesNotThrowAnyException();
    }
  }

  @Test
  @DisplayName("Testing registerMovement")
  public void registerMovement() {
    final BigDecimal amount = BigDecimal.valueOf(-1000);
    //given
    RegisterMovementRequest registerMovementRequest = generateRegisterMovementRequest(amount);
    Account account = generateAccount();
    BigDecimal currentBalance = account.getCurrentBalance();
    Movement movement = movementMapper.movementDtoToMovement(registerMovementRequest);
    movement.setMovementId(1L);
    when(accountRepository.getAccountIfExists((anyLong()))).thenReturn(account);
    when(movementRepository.save(any(Movement.class))).thenReturn(movement);
    //when
    var result = movementServiceImpl.registerMovement(registerMovementRequest);
    //then
    verify(accountRepository, times(1)).getAccountIfExists(anyLong());
    verify(movementRepository, times(1)).save(any(Movement.class));
    verify(accountRepository, times(1)).save(any(Account.class));
    assertThat(account.getCurrentBalance()).isEqualTo(currentBalance.add(amount));
    assertThat(result).isNotNull();
    assertThat(result.getMovementId()).isEqualTo(1L);
    assertThat(account.getTodayTotalWithdrawal()).isEqualTo(amount.abs());
  }

  private RegisterMovementRequest generateRegisterMovementRequest(BigDecimal amount) {
    RegisterMovementRequest movement = new RegisterMovementRequest();
    movement.setAmount(amount);
    movement.setAccountId(1L);
    return movement;
  }
}