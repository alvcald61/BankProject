package com.bp.appbanco.controller;

import com.bp.appbanco.errors.EntityNotFoundException;
import com.bp.appbanco.errors.NumericErrorException;
import com.bp.appbanco.service.dto.response.ApiError;
import com.bp.appbanco.service.dto.response.ApiResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

import static com.bp.appbanco.errors.ErrorManager.MALFORMED_JSON_REQUEST;

@ControllerAdvice
@Log4j2
public class ErrorHandlerController extends ResponseEntityExceptionHandler {

  private ResponseEntity<Object> buildResponseEntity(ApiResponse<ApiError> apiResponse) {
    return new ResponseEntity<>(apiResponse, apiResponse.getError().getStatus());
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    log.error("Malformed JSON request", ex);
    ApiResponse<ApiError> response = new ApiResponse<>(new ApiError(HttpStatus.BAD_REQUEST, errorBody(MALFORMED_JSON_REQUEST.getMessage())));
    return buildResponseEntity(response);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public ResponseEntity<Object> handleUserNotFoundException(EntityNotFoundException ex) {
    log.error("Entity not found", ex);
    ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, errorBody(ex.getMessage()));
    ApiResponse<ApiError> response = new ApiResponse<>(apiError);
    return buildResponseEntity(response);
  }

  @ExceptionHandler(value = {IllegalArgumentException.class, NumericErrorException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ResponseEntity<Object> handleIllegalArgumentException(Exception ex) {
    log.error("Illegal argument", ex);
    ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, errorBody(ex.getMessage()));
    ApiResponse<ApiError> response = new ApiResponse<>(apiError);
    return buildResponseEntity(response);
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    log.error("Missing request parameter", ex);
    ApiResponse<ApiError> response = new ApiResponse<>(new ApiError(HttpStatus.BAD_REQUEST, errorBody(ex.getParameterName() + " parameter is missing")));
    return buildResponseEntity(response);
  }

  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    log.error("URL not found", ex);
    ApiResponse<ApiError> response = new ApiResponse<>(new ApiError(HttpStatus.NOT_FOUND, errorBody(ex.getMessage())));
    return buildResponseEntity(response);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    log.error("Method argument not valid", ex);
    var exceptionErrors = ex.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
    ApiResponse<ApiError> response = new ApiResponse<>(new ApiError(HttpStatus.BAD_REQUEST, errorBody(exceptionErrors)));
    return buildResponseEntity(response);
  }
  
  
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public ResponseEntity<Object> handleException(Exception ex) {
    log.error("Internal server error", ex);
    ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, errorBody(ex.getMessage()));
    ApiResponse<ApiError> response = new ApiResponse<>(apiError);
    return buildResponseEntity(response);
  }
  
  private List<String> errorBody(String error) {
    List<String> errors = new ArrayList<>();
    errors.add(error);
    return errors;
  }
  
private List<String> errorBody(List<String> exceptionErrors) {
    return exceptionErrors;
  }
}
