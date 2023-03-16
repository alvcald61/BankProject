package com.bp.appbanco.model.person;

import com.bp.appbanco.model.account.Account;
import com.bp.appbanco.model.emuns.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Set;



@Entity
@Getter
@Setter
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "client_id")
@SQLDelete(sql = "UPDATE client SET status = 'INACTIVE' WHERE client_id = ? ")
@Where(clause = "status = 'ACTIVE'") 
public class Client extends Person {
  private String password;
  @Enumerated(EnumType.STRING)
  private Status status = Status.ACTIVE;
  @OneToMany(mappedBy = "client")
  private Set<Account> accounts;
}
