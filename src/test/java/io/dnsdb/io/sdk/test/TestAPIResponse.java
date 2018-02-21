package io.dnsdb.io.sdk.test;

import static org.junit.Assert.assertEquals;

import io.dnsdb.sdk.exceptions.APIException;
import io.dnsdb.sdk.responses.APIResponse;
import org.junit.Test;

/**
 * <code>TestAPIResponse</code>用于测试{@link io.dnsdb.sdk.responses.APIResponse}
 *
 * @author Remonsan
 * @version 1.0
 */
public class TestAPIResponse {

  @Test(expected = APIException.class)
  public void testCheckError() throws APIException {
    APIResponse apiResponse = new APIResponse().setErrorCode(10001).setErrorMsg("unauthorized");
    apiResponse.checkError();
  }

  @Test
  public void testCheckErrorWithNoException() throws APIException {
    APIResponse apiResponse = new APIResponse();
    apiResponse.checkError();
  }

  @Test
  public void testGetterSetter() {
    Integer code = 10001;
    String msg = "unauthorized";
    String doc = "https://apidoc.dnsdb.io";
    APIResponse apiResponse = new APIResponse().setErrorCode(code).setErrorMsg(msg).setDoc(doc);
    assertEquals(code, apiResponse.getErrorCode());
    assertEquals(msg, apiResponse.getErrorMsg());
    assertEquals(doc, apiResponse.getDoc());
  }
}
