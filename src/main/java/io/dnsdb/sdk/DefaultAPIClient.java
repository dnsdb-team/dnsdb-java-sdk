package io.dnsdb.sdk;

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import io.dnsdb.sdk.exceptions.APIException;
import io.dnsdb.sdk.responses.APIResponse;
import io.dnsdb.sdk.responses.APIUserResponse;
import io.dnsdb.sdk.responses.ScanResponse;
import io.dnsdb.sdk.responses.SearchResponse;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * <code>DefaultAPIClient</code>实现了{@link APIClient}接口。
 *
 * @author Remonsan
 * @version 1.0
 * @see io.dnsdb.sdk.APIClient
 */
public class DefaultAPIClient implements APIClient {

  private CloseableHttpClient httpClient;
  private String apiId;
  private String apiKey;
  private final ObjectMapper objectMapper = new ObjectMapper();
  private RequestConfig requestConfig;

  public DefaultAPIClient(String apiId, String apiKey, RequestConfig requestConfig) {
    this.apiId = checkNotNull(apiId);
    this.apiKey = checkNotNull(apiKey);
    this.httpClient = HttpClients.createDefault();
    this.requestConfig = requestConfig;
  }

  public DefaultAPIClient(String apiId, String apiKey) {
    this(apiId, apiKey,
        RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(1000)
            .setSocketTimeout(5000).build());
  }

  private APIResponse doGet(String uri, Class<? extends APIResponse> responseClass)
      throws IOException {
    HttpGet httpGet = new HttpGet(uri);
    httpGet.setHeader("API-ID", this.apiId);
    httpGet.setHeader("API-Key", this.apiKey);
    if (requestConfig != null) {
      httpGet.setConfig(requestConfig);
    }
    try (CloseableHttpResponse response = this.httpClient.execute(httpGet)) {
      HttpEntity entity = response.getEntity();
      if (entity != null) {
        String content = EntityUtils.toString(entity);
        return objectMapper.readValue(content, responseClass);
      } else {
        throw new IOException("No response data");
      }
    }
  }


  @Override
  public SearchResult search(Query query, int page, int pageSize) throws APIException, IOException {
    Preconditions.checkNotNull(query);
    String param = new URLParameterBuilder().put("domain", query.getDomain())
        .put("type", query.getType())
        .put("ip", query.getIp())
        .put("host", query.getHost())
        .put("value_domain", query.getValueDomain())
        .put("value_host", query.getValueHost())
        .put("value_ip", query.getValueIp())
        .put("email", query.getEmail())
        .put("page", page)
        .put("size", pageSize)
        .build();
    SearchResponse response = (SearchResponse) doGet(getUrl("dns/search") + param,
        SearchResponse.class);
    if (response.hasError()) {
      throw new APIException(response.getErrorCode(), response.getErrorMsg());
    }
    return new SearchResult(response.getRecords(), response.getRemainingRequests(),
        response.getTotal());
  }


  @Override
  public ScanResponse createScan(Query query, int perSize) throws IOException {
    String param = new URLParameterBuilder().put("domain", query.getDomain())
        .put("type", query.getType())
        .put("ip", query.getIp())
        .put("host", query.getHost())
        .put("value_domain", query.getValueDomain())
        .put("value_host", query.getValueHost())
        .put("value_ip", query.getValueIp())
        .put("email", query.getEmail())
        .put("size", perSize + "")
        .build();
    return (ScanResponse) doGet(getUrl("dns/scan/create") + param, ScanResponse.class);
  }

  @Override
  public ScanResponse nextScan(String scanId) throws IOException {
    String param = new URLParameterBuilder().put("scan_id", scanId).build();
    return (ScanResponse) doGet(getUrl("dns/scan/next" + param), ScanResponse.class);
  }

  @Override
  public ScanResult scan(Query query, int perSize) throws APIException, IOException {
    ScanResponse response = createScan(query, perSize);
    if (response.hasError()) {
      throw new APIException(response.getErrorCode(), response.getErrorMsg());
    }
    return new ScanResult(response, this);
  }

  @Override
  public APIUser getAPIUser() throws APIException, IOException {
    APIUserResponse response = (APIUserResponse) doGet(getUrl("api_user"), APIUserResponse.class);
    if (response.hasError()) {
      throw new APIException(response.getErrorCode(), response.getErrorMsg());
    }
    return new APIUser(response.getApiId(), response.getUser(), response.getRemainingRequests(),
        response.getCreationTime(), response.getExpirationTime());
  }

  public CloseableHttpClient getHttpClient() {
    return httpClient;
  }

  public DefaultAPIClient setHttpClient(CloseableHttpClient httpClient) {
    this.httpClient = httpClient;
    return this;
  }

  public String getApiId() {
    return apiId;
  }

  public DefaultAPIClient setApiId(String apiId) {
    this.apiId = apiId;
    return this;
  }

  public String getApiKey() {
    return apiKey;
  }

  public DefaultAPIClient setApiKey(String apiKey) {
    this.apiKey = apiKey;
    return this;
  }

  public RequestConfig getRequestConfig() {
    return requestConfig;
  }

  public DefaultAPIClient setRequestConfig(RequestConfig requestConfig) {
    this.requestConfig = requestConfig;
    return this;
  }
}

