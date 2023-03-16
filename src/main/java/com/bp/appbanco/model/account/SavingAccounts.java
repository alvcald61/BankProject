package com.bp.appbanco.model.account;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
public class SavingAccounts extends Account{
  public SavingAccounts() {
  }
}
