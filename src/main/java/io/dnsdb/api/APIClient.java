package io.dnsdb.api;

import java.io.IOException;

import io.dnsdb.api.exceptions.APIException;
import io.dnsdb.api.responses.ScanResponse;

import static io.dnsdb.api.APIManager.API_BASE_URL;
import static io.dnsdb.api.APIManager.API_VERSION;

/**
 * <code>APIClient</code>接口表示一个API客户端。
 *
 * @author Remonsan
 * @version 1.0
 */
public interface APIClient {

  /**
   * 根据搜索条件查询DNS记录，并返回指定页码的查询结果。注意方法最多获取前10000条查询结果。
   * 每调用一次该方法，当前API User的API请求次数减1。
   *
   * @param query    查询条件
   * @param page     页码
   * @param pageSize 页大小
   * @return 查询结果
   * @throws APIException API级别的异常
   * @throws IOException  IO异常
   */
  SearchResult search(Query query, int page, int pageSize) throws APIException, IOException;

  /**
   * 根据搜索条件创建一个Scan DNS任务。
   * 每调用一次该方法，当前API User的API请求次数减1。
   *
   * @param query   查询条件
   * @param perSize 每次scan的最大结果数量
   * @return {@link ScanResponse}对象
   * @throws IOException IO异常
   */
  ScanResponse createScan(Query query, int perSize) throws IOException;

  /**
   * 获取指定scan ID的下一次scan结果。
   * 每调用一次该方法，当前API User的API请求次数减1。
   *
   * @param scanId Scan ID
   * @return {@link ScanResponse}对象
   * @throws IOException IO异常
   */
  ScanResponse nextScan(String scanId) throws IOException;

  /**
   * 根据查询条件，创建一个可以迭代的自动查询DNS的{@link ScanResult}对象。
   * 每调用一次该方法，当前API User的API请求次数减1。
   *
   * @param query   查询条件
   * @param perSize 每次Scan的最大结果数
   * @return {@link ScanResult}对象
   * @throws APIException API级别异常
   * @throws IOException  IO异常
   */
  ScanResult scan(Query query, int perSize) throws APIException, IOException;

  /**
   * 获取当前API User的信息。
   *
   * @return {@link APIUser}对象
   * @throws APIException API级别异常
   * @throws IOException  IO异常
   */
  APIUser getAPIUser() throws APIException, IOException;

  default String getUrl(String subPath) {
    return API_BASE_URL + '/' + API_VERSION + '/' + subPath;
  }

  /**
   * 根据查询条件，创建一个可以迭代的自动查询DNS的{@link ScanResult}对象。
   * 每调用一次该方法，当前API User的API请求次数减1。
   *
   * @param query 查询条件
   * @return {@link ScanResult}对象。
   * @throws APIException API级别异常
   * @throws IOException  IO异常
   */
  default ScanResult scan(Query query) throws APIException, IOException {
    return scan(query, 50);
  }

  /**
   * 根据搜索条件查询DNS记录，并返回指定页码的查询结果。注意方法最多获取前10000条查询结果。
   * 每调用一次该方法，当前API User的API请求次数减1。
   *
   * @param query 查询条件
   * @param page  页码
   * @return 查询结果
   * @throws APIException API级别异常
   * @throws IOException  IO异常
   */
  default SearchResult search(Query query, int page) throws APIException, IOException {
    return search(query, page, 50);
  }
}
