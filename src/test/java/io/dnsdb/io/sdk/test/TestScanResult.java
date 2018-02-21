package io.dnsdb.io.sdk.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import com.google.common.collect.Lists;
import io.dnsdb.sdk.APIClient;
import io.dnsdb.sdk.APIClientBuilder;
import io.dnsdb.sdk.ScanResult;
import io.dnsdb.sdk.exceptions.IteratorException;
import io.dnsdb.sdk.responses.ScanResponse;
import java.io.IOException;
import java.util.UUID;
import org.junit.Test;

/**
 * <code>TestScanResult</code>用于测试{@link io.dnsdb.sdk.ScanResult}。
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

  @Test(expected = IteratorException.class)
  public void testNext() throws IOException {
    String scanId = UUID.randomUUID().toString().replace("_", "");
    APIClient client = mock(APIClient.class);
    doThrow(new IOException()).when(client).nextScan(scanId);
    ScanResponse response = new ScanResponse();
    response.setRecords(Lists
        .newArrayList())
        .setTotal(5)
        .setScanId(scanId);
    ScanResult result = new ScanResult(response, client);
    result.iterator().next();
  }
}
