package io.dnsdb.api;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * <code>APIClientBuilder</code>类用于构造合适的{@link APIClient}实例。
 *
 * @author Remonsan
 * @version 1.0
 */
public class APIClientBuilder {
  private String apiId;
  private String apiKey;
  private CloseableHttpClient client;
  private RequestConfig requestConfig;

  /**
   * 构造器。
   *
   * @param apiId  API ID
   * @param apiKey API key
   */
  public APIClientBuilder(String apiId, String apiKey) {
    this.apiId = apiId;
    this.apiKey = apiKey;
  }

  public APIClientBuilder setClient(CloseableHttpClient client) {
    this.client = client;
    return this;
  }

  public APIClientBuilder setRequestConfig(RequestConfig requestConfig) {
    this.requestConfig = requestConfig;
    return this;
  }

  /**
   * 创建{@link APIClient}实例。
   *
   * @return {@link APIClient}实例
   */
  public APIClient build() {
    DefaultAPIClient apiClient = new DefaultAPIClient(apiId, apiKey);
    if (client != null) {
      apiClient.setHttpClient(client);
    }
    if (requestConfig != null) {
      apiClient.setRequestConfig(requestConfig);
    }
    return apiClient;
  }
}
