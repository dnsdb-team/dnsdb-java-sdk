package io.dnsdb.io.sdk.test;

import static org.junit.Assert.assertEquals;

import io.dnsdb.sdk.APIManager;
import org.junit.Test;

/**
 * <code>TestAPIManager</code>用于测试{@link io.dnsdb.sdk.APIManager}
 *
 * @author Remonsan
 */
public class TestAPIManager extends APIManager {

  @Test
  public void testRestAPIBaseURL() {
    APIManager.API_BASE_URL = "http://localhost:8080";
    APIManager.resetAPIBaseURL();
    assertEquals(APIManager.DEFAULT_API_BASE_URL, APIManager.API_BASE_URL);

  }
}
