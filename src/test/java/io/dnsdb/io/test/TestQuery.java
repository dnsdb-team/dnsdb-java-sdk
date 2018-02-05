package io.dnsdb.io.test;

import org.junit.Test;

import io.dnsdb.api.Query;

import static org.junit.Assert.assertEquals;

/**
 * <code>TestQuery</code>用于测试{@link Query}
 *
 * @author Remonsan
 */
public class TestQuery {

  @Test
  public void testGetterSetter() {
    String host = "www.google.com";
    String domain = "google.com";
    String type = "A";
    String ip = "1.1.1.1";
    String valueDomain = "google.com";
    String valueHost = "map.google.com";
    String valueIp = "8.8.8.8";
    String email = "admin@google.com";
    Query query = new Query().setHost(host).setDomain(domain).setType(type).setIp(ip).setValueDomain(valueDomain)
            .setValueHost(valueHost).setValueIp(valueIp).setEmail(email);
    assertEquals(host, query.getHost());
    assertEquals(domain, query.getDomain());
    assertEquals(type, query.getType());
    assertEquals(ip, query.getIp());
    assertEquals(valueDomain, query.getValueDomain());
    assertEquals(valueHost, query.getValueHost());
    assertEquals(valueIp, query.getValueIp());
    assertEquals(email, query.getEmail());

  }


}
