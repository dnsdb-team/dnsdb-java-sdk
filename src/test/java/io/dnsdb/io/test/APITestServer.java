package io.dnsdb.io.test;

import com.google.common.collect.Queues;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Queue;

import fi.iki.elonen.NanoHTTPD;
import io.dnsdb.api.responses.APIResponse;

/**
 * <code>APITestServer</code>是一个API测试服务器。
 *
 * @author Remonsan
 * @version 1.0
 */
public class APITestServer extends NanoHTTPD {
  private final ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
  private Queue<APIResponse> apiUserResponses = Queues.newConcurrentLinkedQueue();
  private Queue<APIResponse> scanCreateResponses = Queues.newConcurrentLinkedQueue();
  private Queue<APIResponse> scanNextResponses = Queues.newConcurrentLinkedQueue();
  private Queue<APIResponse> searchResponses = Queues.newConcurrentLinkedQueue();

  public APITestServer(int port) {
    super(port);
  }

  public void start() throws IOException {
    start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
  }

  private Response newJsonResponse(Object object) {
    try {
      String json = objectMapper.writeValueAsString(object);
      return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/json", json);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }


  @Override
  public Response serve(IHTTPSession session) {
    String path = session.getUri();
    switch (path) {
      case "/v1/api_user":
        return newJsonResponse(apiUserResponses.poll());
      case "/v1/dns/search":
        return newJsonResponse(searchResponses.poll());
      case "/v1/dns/scan/create":
        return newJsonResponse(scanCreateResponses.poll());
      case "/v1/dns/scan/next":
        return newJsonResponse(scanNextResponses.poll());
    }

    return newJsonResponse(new APIResponse().setErrorCode(10201).setErrorMsg("request wrong url").setDoc("https://apidoc.dnsdb.io"));
  }

  public Queue<APIResponse> getApiUserResponses() {
    return apiUserResponses;
  }

  public APITestServer setApiUserResponses(Queue<APIResponse> apiUserResponses) {
    this.apiUserResponses = apiUserResponses;
    return this;
  }

  public Queue<APIResponse> getScanCreateResponses() {
    return scanCreateResponses;
  }

  public APITestServer setScanCreateResponses(Queue<APIResponse> scanCreateResponses) {
    this.scanCreateResponses = scanCreateResponses;
    return this;
  }

  public Queue<APIResponse> getScanNextResponses() {
    return scanNextResponses;
  }

  public APITestServer setScanNextResponses(Queue<APIResponse> scanNextResponses) {
    this.scanNextResponses = scanNextResponses;
    return this;
  }

  public Queue<APIResponse> getSearchResponses() {
    return searchResponses;
  }

  public APITestServer setSearchResponses(Queue<APIResponse> searchResponses) {
    this.searchResponses = searchResponses;
    return this;
  }
}

