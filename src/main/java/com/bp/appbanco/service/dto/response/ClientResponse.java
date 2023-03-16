package com.bp.appbanco.service.dto.response;

import com.bp.appbanco.model.emuns.Status;
import com.bp.appbanco.model.person.Client;
import lombok.*;


/**
 * A DTO for the {@link Client} entity
 */
@Data
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse {
  private Long personId;
  private String name;
  private String gender;
  private Integer age;
  private String identifier;
  private String address;
  private String phone;
  private Status status;
}