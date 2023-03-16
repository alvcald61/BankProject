package com.bp.appbanco.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private T data;
    private boolean hasErrors;
    private ApiError error;



  public ApiResponse(ApiError error) {
    this.hasErrors = true;
    this.error = error;
  }
  public ApiResponse(T data) {
    this.data = data;
    this.hasErrors = false;
  }
}
