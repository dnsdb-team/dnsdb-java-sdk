package io.dnsdb.sdk;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * <code>URLParameterBuilder</code>类用于构造URL参数字符串。
 *
 * @author Remonsan
 * @version 1.0
 */
public class URLParameterBuilder {

  private Map<String, Object> parameters = new HashMap<>();
  private String charsetName = Charset.defaultCharset().name();

  public URLParameterBuilder put(String name, Object value) {
    parameters.put(name, value);
    return this;
  }

  private String urlEncode(String content) {
    try {
      return URLEncoder.encode(content, charsetName);
    } catch (UnsupportedEncodingException ignored) {
    }
    return null;
  }

  public String build() {
    StringBuilder stringBuilder = new StringBuilder("?");
    for (String name : parameters.keySet()) {
      Object value = parameters.get(name);
      if (value != null) {
        String parameterValue = value.toString();
        parameterValue = urlEncode(parameterValue);
        if (parameterValue != null) {
          stringBuilder.append(String.format("%s=%s", name, parameterValue));
        }
      }
    }
    return stringBuilder.toString();
  }

  public String getCharsetName() {
    return charsetName;
  }

  public URLParameterBuilder setCharsetName(String charsetName) {
    this.charsetName = charsetName;
    return this;
  }
}
