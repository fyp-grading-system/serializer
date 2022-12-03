package com.fypgradingsystem.serializer.messaging;

import io.vertx.core.json.JsonObject;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Message {
  private String queue;
  private String type;
  private String order;

  public String getMessage() {
    var json = new JsonObject();
    json.put("type", type);
    json.put("order", order);
    return json.toString();
  }
}
