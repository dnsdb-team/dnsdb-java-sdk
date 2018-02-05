package io.dnsdb.io.test;

import org.junit.Test;

import io.dnsdb.api.APIManager;

import static org.junit.Assert.assertEquals;

/**
 * <code>TestAPIManager</code>用于测试{@link io.dnsdb.api.APIManager}
 *
 * @author Remonsan
 */
public class TestAPIManager {

  @Test
  public void testRestAPIBaseURL() {
    APIManager.API_BASE_URL = "http://localhost:8080";
    APIManager.resetAPIBaseURL();
    assertEquals(APIManager.DEFAULT_API_BASE_URL, APIManager.API_BASE_URL);

  }
}
