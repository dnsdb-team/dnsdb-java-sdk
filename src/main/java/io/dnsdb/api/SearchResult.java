package io.dnsdb.api;

import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <code>SearchResult</code>表示一次search结果。
 *
 * @author Remonsan
 * @version 1.0
 */
public class SearchResult implements Iterable<DNSRecord> {
  private final List<DNSRecord> records;
  private final int remainingRequests;
  private final long total;

  public SearchResult(List<DNSRecord> records, int remainingRequests, long total) {
    this.records = checkNotNull(records);
    this.remainingRequests = remainingRequests;
    this.total = total;
  }

  public Iterator<DNSRecord> iterator() {
    return records.iterator();
  }

  public int size() {
    return records.size();
  }

  public List<DNSRecord> getRecords() {
    return records;
  }

  public int getRemainingRequests() {
    return remainingRequests;
  }

  public long getTotal() {
    return total;
  }
}
