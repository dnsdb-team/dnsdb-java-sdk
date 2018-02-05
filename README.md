# dnsdb-java-sdk
[![Build Status](https://travis-ci.org/dnsdb-team/dnsdb-java-sdk.svg?branch=master)](https://travis-ci.org/dnsdb-team/dnsdb-java-sdk)
[![Coverage Status](https://coveralls.io/repos/github/dnsdb-team/dnsdb-java-sdk/badge.svg?branch=master)](https://coveralls.io/github/dnsdb-team/dnsdb-java-sdk?branch=master)

dnsdb-java-sdk是一个实现[DNSDB API v1](https://apidoc.dnsdb.io)所有接口的Java库。你可以使用该Java库轻松方便的调用[DNSDB API v1](https://apidoc.dnsdb.io)。



其他语言实现的SDK：

|    语言   |      项目     |
| --------- | ---------------- |
|   python  | [dnsdb-python-sdk](https://pysdk.dnsdb.io) |

# Require

* JDK 8+
* Apache Maven 3+
* 在[https://dnsdb.io](https://dnsdb.io)上已经创建至少一个API User，并且该API User有可用的API请求次数

# Quick Start

1. 克隆dnsdb-python-sdk到本地
    ```bash
    git clone git@github.com:dnsdb-team/dnsdb-java-sdk.git
    ```
2. 安装到本地仓库
    ```bash
    cd dnsdb-java-sdk
    mvn clean install
    ```
3. 在您的项目中pom.xml中的`dependencies`元素下增加以下配置
    ```xml
    <dependency>
         <groupId>io.dnsdb.sdk</groupId>
         <artifactId>dnsdb-java-sdk</artifactId>
         <version>1.0-SNAPSHOT</version>
    </dependency>
    ```
 
 ## Example
 
```java
import java.io.IOException;

import io.dnsdb.api.APIClient;
import io.dnsdb.api.APIClientBuilder;
import io.dnsdb.api.APIUser;
import io.dnsdb.api.DNSRecord;
import io.dnsdb.api.Query;
import io.dnsdb.api.ScanResult;
import io.dnsdb.api.SearchResult;
import io.dnsdb.api.exceptions.APIException;

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
* [DNSDB API Documentation](https://apidoc.dnsdb.io)
