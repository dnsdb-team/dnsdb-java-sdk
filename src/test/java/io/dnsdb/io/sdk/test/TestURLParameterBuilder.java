package io.dnsdb.io.sdk.test;

import static org.junit.Assert.assertEquals;

import com.google.common.base.Charsets;
import io.dnsdb.sdk.URLParameterBuilder;
import org.junit.Test;

/**
 * <code>TestURLParameterBuilder</code>测试类用于测试{@link io.dnsdb.sdk.URLParameterBuilder}。
 *
 * @author Remosan
 * @version 1.0
 */
public class TestURLParameterBuilder {

  @Test
  public void test() {
    URLParameterBuilder urlParameterBuilder = new URLParameterBuilder();
    urlParameterBuilder.setCharsetName("utf-888");
    String params = urlParameterBuilder.put("domain", "example.com").build();
    assertEquals("?", params);
  }


  @Test
  public void testGetterSetter() {
    URLParameterBuilder builder = new URLParameterBuilder();
    builder.setCharsetName(Charsets.UTF_8.name());
    assertEquals(Charsets.UTF_8.name(), builder.getCharsetName());
  }

}
