package com.bp.appbanco.errors;

public class ErrorManager {
  public static final NumericErrorException  AMOUNT_GRATHER_THAN_BALANCE = new NumericErrorException("The amount to withdraw is greater than the account balance");
  public static final NumericErrorException  MAX_WITHDRAWAL_EXCEEDED = new NumericErrorException("Maximum withdrawal exceeded");
  public static final NumericErrorException  WITHDRAWAL_WHEN_BALANCE_IS_ZERO = new NumericErrorException("The account balance is zero, you can not make a withdrawal");
  
  public static final Exception MALFORMED_JSON_REQUEST = new Exception("Malformed JSON request");
}
