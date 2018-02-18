package io.dnsdb.sdk;

/**
 * <code>DNSRecordResult</code>接口表示DNS查询结果。
 *
 * @author Remonsan
 * @version 1.0
 */
public interface DNSRecordResult extends Iterable<DNSRecord> {

  /**
   * 获取匹配当前查询条件的所有结果数量。
   *
   * @return 匹配当前查询条件的所有结果数量。
   */
  long getTotal();

  /**
   * 获取可迭代的所有{@link DNSRecord}的数量。
   *
   * @return 当前对象可迭代的所有<code>DNSRecord</code>的数量。
   */
  long size();

  /**
   * 获取剩余API请求次数。
   *
   * @return 剩余API请求次数。
   */
  int getRemainingRequests();
}
