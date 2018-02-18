package io.dnsdb.io.sdk.test;

import org.junit.Test;

import io.dnsdb.sdk.APIClient;
import io.dnsdb.sdk.APIClientBuilder;
import io.dnsdb.sdk.ScanResult;
import io.dnsdb.sdk.responses.ScanResponse;

import static org.junit.Assert.assertEquals;

/**
 * <code>TestScanResult</code>用于测试{@link io.dnsdb.sdk.ScanResult}
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
