package io.getint.recruitment_task.utils.http;

import io.getint.recruitment_task.enums.RequestEnums;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

import static io.getint.recruitment_task.utils.logging.CustomLogger.log;

public final class HttpConnection {
  private HttpConnection() {
    throw new UnsupportedOperationException(
        "HttpConnection can't be instanced - use static methods instead");
  }

  // Execute HTTP request
  public static String execute(HttpRequestBase request) {
    log("Executing " + request.getClass().getSimpleName() + " request for " + request.getURI());

    String result = null;
    try (CloseableHttpClient client = HttpClients.createDefault()) {
      // Actual execution
      HttpResponse response = client.execute(request);
      result = EntityUtils.toString(response.getEntity());

      // Logging
      log(
          "Executed the request. Status code: "
              + response.getStatusLine().getStatusCode()
              + " - "
              + response.getStatusLine().getReasonPhrase());
      log("Response body: " + result);
    } catch (IOException ex) {
      log(Level.SEVERE, "Failed to execute the search request");
      System.exit(1000);
    }
    return Objects.requireNonNull(result);
  }

  // Create new request but without request body
  public static HttpRequestBase create(
      String authentication, String url, RequestEnums route, List<String> params) {
    return create(authentication, url, route, params, null);
  }

  // Create new request with request body support
  public static HttpRequestBase create(
      String authentication, String url, RequestEnums route, List<String> params, String body) {
    // Validate if body for POST request is present
    if (route.name().startsWith("POST") && body == null) {
      log(Level.SEVERE, "Fatal error - null argument provided for the POST request");
      System.exit(1000);
    }

    // Create route
    log("Creating " + route + " request");
    StringBuilder path = new StringBuilder(url + route.value());
    if (params != null) params.forEach(path::append);

    HttpRequestBase request =
        switch (route) {
          case GET_ISSUE, GET_COMMENT -> new HttpGet(path.toString());
          case POST_ISSUE, POST_COMMENT -> new HttpPost(path.toString());
        };

    if (request instanceof HttpPost httpPost) {
      httpPost.setEntity(new StringEntity(body, StandardCharsets.UTF_8));
    }

    request.setHeader("Authorization", "Basic " + authentication);
    request.setHeader("Content-Type", "application/json");
    return request;
  }
}
