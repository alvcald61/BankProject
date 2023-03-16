package com.bp.appbanco.model.account;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CurrentAccount extends Account{
  public CurrentAccount() {
    super();
  } 
}
