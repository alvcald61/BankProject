package com.bp.appbanco.model.person;

import com.bp.appbanco.model.emuns.Gender;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Builder
public class Person {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long personId;
  private String name;
  @Enumerated(EnumType.STRING)
  private Gender gender;
  private Integer age;
  private String identifier;
  private String address;
  private String phone;
  
}
