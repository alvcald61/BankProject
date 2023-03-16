package com.bp.appbanco.model.movements;


import com.bp.appbanco.model.account.Account;
import com.bp.appbanco.model.emuns.MovementType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movement {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long movementId;
  private Date movementDate;
  private BigDecimal amount;
  private BigDecimal initialBalance;
  @ManyToOne
  private Account account;
  @Enumerated(EnumType.STRING)
  private MovementType movementType;

  @PrePersist
  public void prePersist() {
    movementDate = new Date();
  }

  public void updateWithAccountInfo(Account account) {
    this.account = account;
    this.initialBalance = account.getCurrentBalance();
  }

  public boolean isWithdrawal() {
    return movementType.equals(MovementType.WITHDRAWAL);
  }

  public boolean isDeposit() {
    return movementType.equals(MovementType.DEPOSIT);
  }

  public Movement revert() {
    return Movement.builder()
      .movementId(null)
      .amount(amount.multiply(BigDecimal.valueOf(-1)))
      .movementType(movementType.equals(MovementType.WITHDRAWAL) ? MovementType.DEPOSIT : MovementType.WITHDRAWAL)
      .initialBalance(initialBalance)
      .account(account)
      .build();
  }
}
