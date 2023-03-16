package com.bp.appbanco.model.account;


import com.bp.appbanco.model.person.Client;
import com.bp.appbanco.model.movements.Movement;
import com.bp.appbanco.model.emuns.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@SQLDelete(sql = "UPDATE banco_test.account SET status = 'INACTIVE' WHERE account_id = ?")
public abstract class Account {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long accountId;
  private String accountNumber;
  @Enumerated(EnumType.STRING)
  private Status status;
  private BigDecimal initialBalance;
  private BigDecimal currentBalance;
  private BigDecimal todayTotalWithdrawal;
  @ManyToOne(optional = false)
  private Client client;
  @OneToMany(mappedBy = "account")
  private Set<Movement> movements;

  public Account() {
  }

  public Account(String accountNumber, BigDecimal initialBalance, Client client) {
    this.accountNumber = accountNumber;
    this.initialBalance = initialBalance;
    this.currentBalance = initialBalance;
    this.client = client;
    status = Status.ACTIVE;
  }
  

  public void transfer(Movement movement) {
    movements.add(movement);
    this.currentBalance = this.currentBalance.add(movement.getAmount());
    if(movement.isWithdrawal()) {
      this.todayTotalWithdrawal = this.todayTotalWithdrawal.add(movement.getAmount().abs());
    }  
    
  }

  public boolean isValidTrasfer(Movement movement) {
    return movement.getAmount().abs().compareTo(this.currentBalance) <= 0;
  }

  public boolean isTodayMaxWithdrawalExceeded(Movement movement, BigDecimal maxWithdrawal) {
    return this.todayTotalWithdrawal.add(movement.getAmount().abs()).compareTo(maxWithdrawal) > 0;
  }

  public boolean isBalanceZero() {
    return currentBalance.compareTo(BigDecimal.ZERO) == 0;
  }
  
  public void revertTransfer(Movement movement) {
    movements.add(movement);
    this.currentBalance = this.currentBalance.add(movement.getAmount());
    if(movement.isDeposit()) {
      this.todayTotalWithdrawal = this.todayTotalWithdrawal.subtract(movement.getAmount().abs());
    }  
    
  }
}
