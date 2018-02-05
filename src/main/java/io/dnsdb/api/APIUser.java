package io.dnsdb.api;

import java.util.Date;

/**
 * <code>APIUser</code>表示一个API User
 *
 * @author Remonsan
 * @version 1.0
 */
public class APIUser {
  private final String apiId;
  private final String user;
  private final int remainingRequests;
  private final Date creationTime;
  private final Date expirationTime;

  public APIUser(String apiId, String user, int remainingRequests, Date creationTime, Date expirationTime) {
    this.apiId = apiId;
    this.user = user;
    this.remainingRequests = remainingRequests;
    this.creationTime = creationTime;
    this.expirationTime = expirationTime;
  }

  public String getApiId() {
    return apiId;
  }

  public String getUser() {
    return user;
  }

  public int getRemainingRequests() {
    return remainingRequests;
  }

  public Date getCreationTime() {
    return creationTime;
  }

  public Date getExpirationTime() {
    return expirationTime;
  }

  @Override
  public String toString() {
    return "APIUser{" +
            "apiId='" + apiId + '\'' +
            ", user='" + user + '\'' +
            ", remainingRequests=" + remainingRequests +
            ", creationTime=" + creationTime +
            ", expirationTime=" + expirationTime +
            '}';
  }
}
