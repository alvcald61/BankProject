package com.bp.appbanco.service.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class Utils {
  private static final String DATE_FORMAT = "dd/MM/yyyy";
  public static <T> T returnFirstNotNull(T obj1, T obj2) {
    return Optional.ofNullable(obj1).orElse(obj2);
  }

  public static String formatDateToString(Object v) {
    
    if (v != null)
      return new SimpleDateFormat(DATE_FORMAT).format(v);
    else return "";
  }
  //mindfulness
  public static Date parseStringToDate(String v) {
    try {
      return new SimpleDateFormat(DATE_FORMAT).parse(v);
    } catch (ParseException e) {
      throw new IllegalArgumentException("Invalid date format: Should be dd/MM/YYYY-dd/MM/YYYY");
    }
  }
}
