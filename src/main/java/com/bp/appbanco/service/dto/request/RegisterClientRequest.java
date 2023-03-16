package com.bp.appbanco.service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterClientRequest {
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long id;
  @NotBlank(message = "Name is required")
  private String name;
  @NotBlank(message = "Address is required")
  private String address;
  @NotBlank(message = "Phone is required")
  private String phone;
  @NotBlank(message = "Password is required")
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;
  private String gender;
  private String identifier;
  @Positive(message = "Age must be greater than 0")
  private Integer age;

  public boolean isValidAge(){
    return age != null && age > 0;
  }
}
