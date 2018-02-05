package io.dnsdb.api;

/**
 * <code>DNSRecord</code>表示一条DNS记录。
 *
 * @author Remonsan
 * @version 1.0
 */
public class DNSRecord {
  private String host;
  private String type;
  private String value;

  public DNSRecord() {
  }

  public DNSRecord(String host, String type, String value) {
    this.host = host;
    this.type = type;
    this.value = value;
  }

  public String getHost() {
    return host;
  }

  public DNSRecord setHost(String host) {
    this.host = host;
    return this;
  }

  public String getType() {
    return type;
  }

  public DNSRecord setType(String type) {
    this.type = type;
    return this;
  }

  public String getValue() {
    return value;
  }

  public DNSRecord setValue(String value) {
    this.value = value;
    return this;
  }

  @Override
  public String toString() {
    return "DNSRecord{" +
            "host='" + host + '\'' +
            ", type='" + type + '\'' +
            ", value='" + value + '\'' +
            '}';
  }
}
