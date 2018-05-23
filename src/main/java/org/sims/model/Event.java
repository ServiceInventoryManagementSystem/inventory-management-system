package org.sims.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

@Component
public class Event {
  @JsonProperty("event")
  private SpecificNotification specificNotification;

  private String eventType;

  public SpecificNotification getSpecificNotification() {
    return specificNotification;
  }

  public void setSpecificNotification(SpecificNotification specificNotification) {
    this.specificNotification = specificNotification;
  }

  public String getEventType() {
    return eventType;
  }

  public void setEventType(String eventType) {
    this.eventType = eventType;
  }
}
