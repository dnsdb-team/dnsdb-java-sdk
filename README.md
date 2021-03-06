# dnsdb-java-sdk
[![Build Status](https://travis-ci.org/dnsdb-team/dnsdb-java-sdk.svg?branch=master)](https://travis-ci.org/dnsdb-team/dnsdb-java-sdk)
[![Coverage Status](https://coveralls.io/repos/github/dnsdb-team/dnsdb-java-sdk/badge.svg?branch=master)](https://coveralls.io/github/dnsdb-team/dnsdb-java-sdk?branch=master)
[![JitPack](https://jitpack.io/v/dnsdb-team/dnsdb-java-sdk.svg)](https://jitpack.io/#dnsdb-team/dnsdb-java-sdk)
[![Maven metadata URI](https://img.shields.io/maven-metadata/v/http/central.maven.org/maven2/io/dnsdb/sdk/dnsdb-java-sdk/maven-metadata.xml.svg)](https://repo.maven.apache.org/maven2/io/dnsdb/sdk/dnsdb-java-sdk/)
[![Maven Central](https://img.shields.io/maven-central/v/org.apache.maven/apache-maven.svg)](https://maven.apache.org)
[![JDK](https://img.shields.io/badge/jdk-8%2B-blue.svg)](http://www.oracle.com/technetwork/java/javase/downloads)
[![License](https://img.shields.io/github/license/dnsdb-team/dnsdb-java-sdk.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)

**dnsdb-java-sdk** 是一个实现[DNSDB API v1](https://apidoc.dnsdb.io)所有接口的Java库。你可以使用该Java库轻松方便的调用[DNSDB Web API v1](https://apidoc.dnsdb.io)。



其他语言实现的SDK：

|    语言   |      项目     |
| --------- | ---------------- |
|   python  | [dnsdb-python-sdk](https://pysdk.dnsdb.io) |

## Requirements

* JDK 8+
* Apache Maven 3+

## Quick Start

### Configures dependencies

Maven

```xml
<dependency>
   <groupId>io.dnsdb.sdk</groupId>
   <artifactId>dnsdb-java-sdk</artifactId>
   <version>0.0.1</version>
</dependency>
```

Gradle

```groovy
compile 'io.dnsdb.sdk:dnsdb-java-sdk:0.0.1'
```

SBT

```sbtshell
libraryDependencies += "io.dnsdb.sdk" % "dnsdb-java-sdk" % "0.0.1"
```

Leiningen

```
[io.dnsdb.sdk/dnsdb-java-sdk "0.0.1"]
```
 
### Example
 
```java
import java.io.IOException;

import io.dnsdb.sdk.APIClient;
import io.dnsdb.sdk.APIClientBuilder;
import io.dnsdb.sdk.APIUser;
import io.dnsdb.sdk.DNSRecord;
import io.dnsdb.sdk.Query;
import io.dnsdb.sdk.ScanResult;
import io.dnsdb.sdk.SearchResult;
import io.dnsdb.sdk.exceptions.APIException;

public class Application {
  public static void main(String[] args) throws APIException, IOException {
    APIClient client = new APIClientBuilder("your API ID", "your API key").build();
    //获取API User信息
    APIUser apiUser = client.getAPIUser();
    //查询google.com的A记录，并获取第一页查询结果
    SearchResult records = client.search(new Query().setDomain("google.com").setType("A"), 1);
    //获取剩余API请求次数
    int remainingRequests = records.getRemainingRequests();
    //遍历查询结果
    for (DNSRecord record : records) {
      System.out.println(record);
    }
    //获取google.com的所有A记录
    ScanResult scanResult = client.scan(new Query().setDomain("google.com").setType("A"));
    for (DNSRecord dnsRecord : scanResult) {
      System.out.println(dnsRecord);
    }
  }
}
```


# Links

* [https://dnsdb.io](https://dnsdb.io)
* [DNSDB Web API Documentation](https://apidoc.dnsdb.io)
