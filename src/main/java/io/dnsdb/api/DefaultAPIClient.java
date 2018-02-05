package io.dnsdb.api;

import com.google.common.base.Preconditions;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import io.dnsdb.api.exceptions.APIException;
import io.dnsdb.api.responses.APIResponse;
import io.dnsdb.api.responses.APIUserResponse;
import io.dnsdb.api.responses.ScanResponse;
import io.dnsdb.api.responses.SearchResponse;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <code>DefaultAPIClient</code>实现了{@link APIClient}接口。
 *
 * @author Remonsan
 * @version 1.0
 * @see io.dnsdb.api.APIClient
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
    this(apiId, apiKey, RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(1000).setSocketTimeout(5000).build());
  }

  private APIResponse doGet(String uri, Class<? extends APIResponse> responseClass) throws IOException {
    HttpGet httpGet = new HttpGet(uri);
    httpGet.setHeader("API-ID", this.apiId);
    httpGet.setHeader("API-Key", this.apiKey);
    if (requestConfig != null) {
      httpGet.setConfig(requestConfig);
    }
    CloseableHttpResponse response = this.httpClient.execute(httpGet);
    try {
      HttpEntity entity = response.getEntity();
      if (entity != null) {
        String content = EntityUtils.toString(entity);
        return objectMapper.readValue(content, responseClass);
      } else {
        throw new IOException("No response data");
      }
    } finally {
      response.close();
    }
  }


  @Override
  public SearchResult search(Query query, int page, int pageSize) throws APIException, IOException {
    Preconditions.checkNotNull(query);
    try {
      URI uri = new URIBuilder().setParameter("domain", query.getDomain())
              .setParameter("type", query.getType())
              .setParameter("ip", query.getIp())
              .setParameter("host", query.getHost())
              .setParameter("value_domain", query.getValueDomain())
              .setParameter("value_host", query.getValueHost())
              .setParameter("value_ip", query.getValueIp())
              .setParameter("email", query.getEmail())
              .setParameter("page", page + "")
              .setParameter("size", pageSize + "")
              .build();
      SearchResponse response = (SearchResponse) doGet(getUrl("dns/search") + uri.toString(), SearchResponse.class);
      if (response.hasError()) {
        throw new APIException(response.getErrorCode(), response.getErrorMsg());
      }
      return new SearchResult(response.getRecords(), response.getRemainingRequests(), response.getTotal());
    } catch (URISyntaxException ignored) {
    }
    return null;
  }


  @Override
  public ScanResponse createScan(Query query, int perSize) throws IOException {
    try {
      URI uri = new URIBuilder().setParameter("domain", query.getDomain())
              .setParameter("type", query.getType())
              .setParameter("ip", query.getIp())
              .setParameter("host", query.getHost())
              .setParameter("value_domain", query.getValueDomain())
              .setParameter("value_host", query.getValueHost())
              .setParameter("value_ip", query.getValueIp())
              .setParameter("email", query.getEmail())
              .setParameter("size", perSize + "")
              .build();
      return (ScanResponse) doGet(getUrl("dns/scan/create") + uri.toString(), ScanResponse.class);
    } catch (URISyntaxException ignored) {
      return null;
    }
  }

  @Override
  public ScanResponse nextScan(String scanId) throws IOException {
    try {
      URI uri = new URIBuilder().setParameter("scan_id", scanId).build();
      return (ScanResponse) doGet(getUrl("dns/scan/next" + uri.toString()), ScanResponse.class);
    } catch (URISyntaxException ignored) {
    }
    return null;
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
    return new APIUser(response.getApiId(), response.getUser(), response.getRemainingRequests(), response.getCreationTime(), response.getExpirationTime());
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

