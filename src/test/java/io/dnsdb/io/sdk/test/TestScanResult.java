package io.dnsdb.io.sdk.test;

import static org.junit.Assert.assertEquals;

import io.dnsdb.sdk.APIClient;
import io.dnsdb.sdk.APIClientBuilder;
import io.dnsdb.sdk.ScanResult;
import io.dnsdb.sdk.responses.ScanResponse;
import org.junit.Test;

/**
 * <code>TestScanResult</code>用于测试{@link io.dnsdb.sdk.ScanResult}
 *
 * @author Remonsan
 * @version 1.0
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
