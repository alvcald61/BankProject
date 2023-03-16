package com.bp.appbanco.service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class ApiError {
  private HttpStatus status;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
  private LocalDateTime timestamp;
  private List<String> errors;
  private ApiError() {
    timestamp = LocalDateTime.now();
  }
  
  public ApiError(HttpStatus status, List<String> error){
    this();
    this.status = status;
    this.errors = error;
  }
}
