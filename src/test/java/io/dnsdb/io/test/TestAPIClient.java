package io.dnsdb.io.test;

import com.google.common.collect.Lists;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import io.dnsdb.api.APIClient;
import io.dnsdb.api.APIClientBuilder;
import io.dnsdb.api.APIManager;
import io.dnsdb.api.APIUser;
import io.dnsdb.api.DNSRecord;
import io.dnsdb.api.DefaultAPIClient;
import io.dnsdb.api.Query;
import io.dnsdb.api.ScanResult;
import io.dnsdb.api.SearchResult;
import io.dnsdb.api.exceptions.APIException;
import io.dnsdb.api.exceptions.IteratorException;
import io.dnsdb.api.responses.APIResponse;
import io.dnsdb.api.responses.APIUserResponse;
import io.dnsdb.api.responses.ScanResponse;
import io.dnsdb.api.responses.SearchResponse;

import static com.google.common.base.Objects.equal;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * <code>TestAPIClient</code>用于测试{@link io.dnsdb.api.DefaultAPIClient}
 *
 * @author Remonsan
 */
public class TestAPIClient {
  private APIClient client;
  private APITestServer server;
  private static final int SERVER_PORT = 58080;

  private String generateUUIDString() {
    return UUID.randomUUID().toString().replace("-", "");
  }

  private APIUserResponse randomAPIUserResponse() {
    return new APIUserResponse().setApiId(UUID.randomUUID().toString().replace("-", ""))
            .setUser("admin")
            .setCreationTime(new Date())
            .setRemainingRequests(Math.abs(new Random().nextInt()))
            .setExpirationTime(new Date());
  }

  private SearchResponse randomSearchResponse() {
    List<DNSRecord> records = Lists.newArrayList();
    for (int i = 0; i < 50; i++) {
      records.add(new DNSRecord().setHost(i + ".google.com").setType("a").setValue(i + ""));
    }
    return new SearchResponse().setRecords(records)
            .setRemainingRequests(Math.abs(new Random().nextInt()))
            .setTotal(50 + Math.abs(new Random().nextInt()) % 1000);
  }

  private boolean equalsDNSRecord(DNSRecord record1, DNSRecord record2) {
    return equal(record1.getHost(), record2.getHost()) && equal(record1.getType(), record2.getType()) && equal(record1.getValue(), record2.getValue());
  }

  private boolean equalDNSRecords(List<DNSRecord> records1, List<DNSRecord> records2) {
    if (records1.size() != records2.size()) {
      return false;
    }
    for (int i = 0; i < records1.size(); i++) {
      if (!equalsDNSRecord(records1.get(i), records2.get(i))) {
        return false;
      }
    }
    return true;
  }


  @Before
  public void setUp() throws IOException, InterruptedException {
    String apiId = generateUUIDString();
    String apiKey = generateUUIDString();
    RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(5000).setConnectionRequestTimeout(1000)
            .setSocketTimeout(5000).build();
    client = new APIClientBuilder(apiId, apiKey).setClient(HttpClients.createDefault())
            .setRequestConfig(requestConfig).build();
    server = new APITestServer(SERVER_PORT);
    server.start();
    Thread.sleep(100);
    APIManager.API_BASE_URL = "http://localhost:" + SERVER_PORT;
  }

  @After
  public void teardown() {
    server.stop();
  }

  @Test
  public void testGetAPIUser() {
    try {
      APIUserResponse response = randomAPIUserResponse();
      server.getApiUserResponses().add(response);
      APIUser user = client.getAPIUser();
      assertEquals(response.getApiId(), user.getApiId());
      assertEquals(response.getUser(), user.getUser());
      assertEquals(response.getRemainingRequests(), user.getRemainingRequests());
      assertEquals(response.getCreationTime(), user.getCreationTime());
      assertEquals(response.getExpirationTime(), user.getExpirationTime());
    } catch (APIException | IOException e) {
      e.printStackTrace();
    }
  }

  @Test(expected = APIException.class)
  public void testGetAPIUserError() throws APIException, IOException {
    server.getApiUserResponses().add(new APIResponse().setErrorCode(10001).setErrorMsg("unauthorized"));
    client.getAPIUser();
  }

  @Test
  public void testSearch() throws APIException, IOException {
    SearchResponse response = randomSearchResponse();
    server.getSearchResponses().add(response);
    SearchResult result = client.search(new Query().setDomain("google.com"), 1);
    assertEquals(response.getTotal(), result.getTotal());
    assertEquals(response.getRemainingRequests(), result.getRemainingRequests());
    assertEquals(response.getRecords().size(), result.size());
    List<DNSRecord> records = new ArrayList<>();
    for (DNSRecord record : result) {
      records.add(record);
    }
    assertTrue(equalDNSRecords(response.getRecords(), result.getRecords()));
    assertTrue(equalDNSRecords(response.getRecords(), records));
  }

  @Test(expected = APIException.class)
  public void testSearchError() throws APIException, IOException {
    server.getSearchResponses().add(new APIResponse().setErrorCode(10001).setErrorMsg("unauthorized"));
    client.search(new Query().setDomain("google.com"), 1);
  }

  @Test
  public void testScan() throws APIException, IOException {
    String scanId = "DnF1ZXJ5VGhlbkZldGNoBQAAAAAAAw";
    List<DNSRecord> records1 = Lists.newArrayList(
            new DNSRecord("www.google.com", "a", "1.1.1.1"),
            new DNSRecord("www.google.com", "a", "1.1.1.1"),
            new DNSRecord("www.google.com", "a", "1.1.1.1")
    );
    List<DNSRecord> records2 = Lists.newArrayList(
            new DNSRecord("www.google.com", "a", "1.1.1.1"),
            new DNSRecord("www.google.com", "a", "1.1.1.1"),
            new DNSRecord("www.google.com", "a", "1.1.1.1")
    );
    List<DNSRecord> records3 = Lists.newArrayList(
            new DNSRecord("www.google.com", "a", "1.1.1.1")
    );
    int total = records1.size() + records2.size() + records3.size();
    ScanResponse scanResponse1 = new ScanResponse().setScanId(scanId).setTotal(total).setRecords(records1).setRemainingRequests(5);
    ScanResponse scanResponse2 = new ScanResponse().setScanId(scanId).setTotal(total).setRecords(records2).setRemainingRequests(4);
    ScanResponse scanResponse3 = new ScanResponse().setScanId(scanId).setTotal(total).setRecords(records3).setRemainingRequests(3);
    server.getScanCreateResponses().add(scanResponse1);
    server.getScanNextResponses().add(scanResponse2);
    server.getScanNextResponses().add(scanResponse3);
    ScanResult result = client.scan(new Query().setDomain("google.com"));
    assertEquals(total, result.getTotal());
    assertEquals(5, result.getRemainingRequests());
    int count = 0;
    for (DNSRecord ignored : result) {
      count++;
    }
    assertEquals(total, count);
    assertEquals(3, result.getRequestedTimes());
    assertEquals(3, result.getRemainingRequests());
  }

  @Test(expected = APIException.class)
  public void testScanAPIError() throws APIException, IOException {
    server.getScanCreateResponses().add(new APIResponse().setErrorCode(10001).setErrorMsg("unauthorized"));
    client.scan(new Query().setDomain("google.com"));
  }

  @Test(expected = IteratorException.class)
  public void testScanResultRepeatIteration() throws APIException, IOException {
    List<DNSRecord> records = Lists.newArrayList(
            new DNSRecord("www.google.com", "a", "1.1.1.1"),
            new DNSRecord("www.google.com", "a", "1.1.1.1"),
            new DNSRecord("www.google.com", "a", "1.1.1.1")
    );
    server.getScanCreateResponses().add(new ScanResponse().setRecords(records).setRemainingRequests(5).setTotal(records.size()));
    ScanResult result = client.scan(new Query().setDomain("google.com"));
    for (DNSRecord ignored : result) {
    }
    for (DNSRecord ignored : result) {
    }
  }

  @Test
  public void testGetterSetter() {
    DefaultAPIClient apiClient = (DefaultAPIClient) client;
    CloseableHttpClient httpClient = HttpClients.createDefault();
    String apiId = generateUUIDString();
    String apiKey = generateUUIDString();
    RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(5000).setConnectionRequestTimeout(1000)
            .setSocketTimeout(5000).build();
    apiClient.setHttpClient(httpClient);
    apiClient.setApiId(apiId);
    apiClient.setApiKey(apiKey);
    apiClient.setRequestConfig(requestConfig);
    assertEquals(httpClient, apiClient.getHttpClient());
    assertEquals(apiId, apiClient.getApiId());
    assertEquals(apiKey, apiClient.getApiKey());
    assertEquals(requestConfig, apiClient.getRequestConfig());
  }

}
