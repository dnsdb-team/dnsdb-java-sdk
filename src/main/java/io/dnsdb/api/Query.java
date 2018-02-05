package io.dnsdb.api;

/**
 * <code>Query</code>类表示DNS查询条件。
 *
 * @author Remonsan
 * @version 1.0
 */
public class Query {
  private String host;
  private String domain;
  private String type;
  private String ip;
  private String valueDomain;
  private String valueHost;
  private String valueIp;
  private String email;

  public String getHost() {
    return host;
  }

  public Query setHost(String host) {
    this.host = host;
    return this;
  }

  public String getDomain() {
    return domain;
  }

  public Query setDomain(String domain) {
    this.domain = domain;
    return this;
  }

  public String getType() {
    return type;
  }

  public Query setType(String type) {
    this.type = type;
    return this;
  }

  public String getIp() {
    return ip;
  }

  public Query setIp(String ip) {
    this.ip = ip;
    return this;
  }

  public String getValueDomain() {
    return valueDomain;
  }

  public Query setValueDomain(String valueDomain) {
    this.valueDomain = valueDomain;
    return this;
  }

  public String getValueHost() {
    return valueHost;
  }

  public Query setValueHost(String valueHost) {
    this.valueHost = valueHost;
    return this;
  }

  public String getValueIp() {
    return valueIp;
  }

  public Query setValueIp(String valueIp) {
    this.valueIp = valueIp;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public Query setEmail(String email) {
    this.email = email;
    return this;
  }
}
