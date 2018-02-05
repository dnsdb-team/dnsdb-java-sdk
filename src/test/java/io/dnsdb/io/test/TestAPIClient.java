package io.dnsdb.io.test;

import com.google.common.collect.Lists;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import io.dnsdb.api.APIClient;
import io.dnsdb.api.APIClientBuilder;
import io.dnsdb.api.APIManager;
import io.dnsdb.api.APIUser;
import io.dnsdb.api.DNSRecord;
import io.dnsdb.api.Query;
import io.dnsdb.api.ScanResult;
import io.dnsdb.api.SearchResult;
import io.dnsdb.api.exceptions.APIException;
import io.dnsdb.api.exceptions.IteratorException;
import io.dnsdb.api.responses.APIResponse;
import io.dnsdb.api.responses.APIUserResponse;
import io.dnsdb.api.responses.ScanResponse;
import io.dnsdb.api.responses.SearchResponse;

import static org.junit.Assert.assertEquals;

/**
 * <code>TestAPIClient</code>用于测试{@link io.dnsdb.api.DefaultAPIClient}
 *
 * @author Remonsan
 */
public class TestAPIClient {
  private APIClient client;
  private APITestServer server;
  private static final int SERVER_PORT = 58080;

  public APIUserResponse randomAPIUserResponse() {
    return new APIUserResponse().setApiId(UUID.randomUUID().toString().replace("-", ""))
            .setUser("admin")
            .setCreationTime(new Date())
            .setRemainingRequests(Math.abs(new Random().nextInt()))
            .setExpirationTime(new Date());
  }

  public SearchResponse randomSearchResponse() {
    List<DNSRecord> records = Lists.newArrayList();
    for (int i = 0; i < 50; i++) {
      records.add(new DNSRecord().setHost(i + ".google.com").setType("a").setValue(i + ""));
    }
    return new SearchResponse().setRecords(records)
            .setRemainingRequests(Math.abs(new Random().nextInt()))
            .setTotal(50 + Math.abs(new Random().nextInt()) % 1000);
  }


  @Before
  public void setUp() throws IOException, InterruptedException {
    String apiId = "";
    String apiKey = "";
    client = new APIClientBuilder(apiId, apiKey).build();
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
  public void testScanError() throws APIException, IOException {
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

}
