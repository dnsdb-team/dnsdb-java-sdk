package io.dnsdb.io.test;

import org.junit.Test;

import io.dnsdb.api.APIClient;
import io.dnsdb.api.APIClientBuilder;
import io.dnsdb.api.ScanResult;
import io.dnsdb.api.responses.ScanResponse;

import static org.junit.Assert.assertEquals;

/**
 * <code>TestScanResult</code>用于测试{@link io.dnsdb.api.ScanResult}
 *
 * @author Remonsan
 */
public class TestScanResult {

  @Test
  public void testGetter() {
    String apiId = "";
    String apiKey = "";
    ScanResponse response = new ScanResponse();
    APIClient client = new APIClientBuilder(apiId, apiKey).build();
    ScanResult result = new ScanResult(response, client);
    assertEquals(response, result.getScanResponse());
    assertEquals(client, result.getClient());

  }
}
