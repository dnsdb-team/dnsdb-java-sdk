package io.dnsdb.api;

import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import io.dnsdb.api.exceptions.IteratorException;
import io.dnsdb.api.responses.ScanResponse;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <code>ScanResult</code>类表示一次scan结果。该类的实例是一个可叠戴对象。通过迭代获取所有的查询结果。
 *
 * @author Remonsan
 * @version 1.0
 */
public class ScanResult implements Iterable<DNSRecord> {
  private final ScanResponse scanResponse;
  private final APIClient client;
  private int remainingRequests;
  private long total;
  private int requestedTimes;
  private boolean alreadyIterated = false;

  public ScanResult(ScanResponse scanResponse, APIClient client) {
    this.scanResponse = checkNotNull(scanResponse);
    this.client = checkNotNull(client);
    this.remainingRequests = scanResponse.getRemainingRequests();
    this.total = scanResponse.getTotal();
    this.requestedTimes = 1;
  }

  @Override
  public Iterator<DNSRecord> iterator() {
    if (alreadyIterated) {
      throw new IteratorException("Already iterated");
    }
    alreadyIterated = true;
    return new ScanIterator();
  }

  public ScanResponse getScanResponse() {
    return scanResponse;
  }

  public APIClient getClient() {
    return client;
  }

  public int getRemainingRequests() {
    return remainingRequests;
  }

  public long getTotal() {
    return total;
  }

  public int getRequestedTimes() {
    return requestedTimes;
  }


  class ScanIterator implements Iterator<DNSRecord> {
    private long count = 0;
    private List<DNSRecord> currentRecords = Lists.newLinkedList(scanResponse.getRecords());
    private String scanId = scanResponse.getScanId();

    @Override
    public boolean hasNext() {
      return count < total;
    }

    @Override
    public DNSRecord next() {
      if (currentRecords.size() == 0) {
        try {
          ScanResponse response = client.nextScan(scanId);
          requestedTimes += 1;
          total = response.getTotal();
          remainingRequests = response.getRemainingRequests();
          this.currentRecords = Lists.newLinkedList(response.getRecords());
          this.scanId = response.getScanId();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      DNSRecord record = currentRecords.get(0);
      currentRecords.remove(0);
      count++;
      return record;
    }
  }
}
