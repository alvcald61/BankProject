package com.bp.appbanco.controller;

import com.bp.appbanco.service.dto.response.ApiError;
import com.bp.appbanco.service.dto.response.ApiResponse;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ResponseWrapper implements ResponseBodyAdvice<Object> {
  @Override
  public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
    return true;
  }
  @Override
  public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
    if (methodParameter.getContainingClass().isAnnotationPresent(RestController.class)) {
      ApiResponse<Object> response;
      if((o instanceof ApiError)){
        response = new ApiResponse<>((ApiError)o);
      }
      else{
        response = new ApiResponse<>(o);
      }
      return response;
    }
    return o;
  }
}
