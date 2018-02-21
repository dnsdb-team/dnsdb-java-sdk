package io.dnsdb.io.sdk.test;

import static org.junit.Assert.assertEquals;

import io.dnsdb.sdk.exceptions.IteratorException;
import java.io.FileNotFoundException;
import org.junit.Test;

/**
 * <code>TestIteratorException</code>测试类用于测试{@link io.dnsdb.sdk.exceptions.IteratorException}。
 *
 * @author Remonsan
 * @version 1.0
 */
public class TestIteratorException {

  @Test
  public void testGetterSetter() {
    FileNotFoundException nestedException = new FileNotFoundException();
    IteratorException exception = new IteratorException("Test exception");
    exception.setNestedException(nestedException);
    assertEquals(nestedException, exception.getNestedException());
  }

}
