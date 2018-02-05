package io.dnsdb.api;

/**
 * <code>APIManager</code>类用于管理API全局设置。
 *
 * @author Remonsan
 * @version 1.0
 */
public abstract class APIManager {
  public static final String DEFAULT_API_BASE_URL = "https://api.dnsdb.io";
  /**
   * API服务器的地址
   */
  public static String API_BASE_URL = DEFAULT_API_BASE_URL;
  /**
   * API接口版本号
   */
  public static String API_VERSION = "v1";

  /**
   * 重置API服务器地址
   */
  public static void resetAPIBaseURL() {
    API_BASE_URL = "https://api.dnsdb.io";
  }
}
