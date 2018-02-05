package io.dnsdb.api.exceptions;

/**
 * <code>APIException</code>表示API级别的异常。
 *
 * @author Remonsan
 * @version 1.0
 */
public class APIException extends Exception {
  public APIException(int errorCode, String errorMsg) {
    super(String.format("error code: %s, message: %s", errorCode, errorMsg));
  }
}
