package com.bp.appbanco.config;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Date;

import static com.bp.appbanco.service.utils.Utils.formatDateToString;

@Configuration
public class ModelMapperConfig {
  Converter<String, BigDecimal> stringToBigDecimal = context -> {
    String source = context.getSource();
    return new BigDecimal(source);
  };

  Converter<BigDecimal, String> bigDecimalToString = context -> {
    BigDecimal source = context.getSource();
    return source.toString();
  };

  Converter<Date, String> formatDate = ctx -> ctx.getSource() != null ? formatDateToString(ctx.getSource()) : "";
  
  @Bean
  public ModelMapper modelMapper() {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.addConverter(stringToBigDecimal);
    modelMapper.addConverter(bigDecimalToString);
    modelMapper.addConverter(formatDate);
    return modelMapper;
  }

}
