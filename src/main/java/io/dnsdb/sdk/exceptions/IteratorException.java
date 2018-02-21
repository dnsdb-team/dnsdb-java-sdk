package io.dnsdb.sdk.exceptions;

/**
 * <code>IteratorException</code>表示迭代异常。
 *
 * @author Remonsan
 * @version 1.0
 */
public class IteratorException extends RuntimeException {

  private Exception nestedException;

  public IteratorException(String message) {
    super(message);
  }

  public IteratorException(Exception nestedException) {
    super(nestedException.getMessage());
    this.nestedException = nestedException;
  }

  public Exception getNestedException() {
    return nestedException;
  }

  public void setNestedException(Exception nestedException) {
    this.nestedException = nestedException;
  }
}
