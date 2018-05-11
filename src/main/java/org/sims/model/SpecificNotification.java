package org.sims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
public class SpecificNotification {
  @JsonProperty("eventId")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;

  private String eventTime;
  private String eventType;

  @JsonIgnore
  private String fieldPath;
  @JsonIgnore
  private String resourcePath;

  @JsonProperty("event")
  @ManyToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "specific_event_id")
  private SpecificEvent specificEvent;

  public String getEventTime() {
    return eventTime;
  }

  public void setEventTime(String eventTime) {
    this.eventTime = eventTime;
  }

  public String getEventType() {
    return eventType;
  }

  public void setEventType(String eventType) {
    this.eventType = eventType;
  }

  public String getFieldPath() {
    return fieldPath;
  }

  public void setFieldPath(String fieldPath) {
    this.fieldPath = fieldPath;
  }

  public String getResourcePath() {
    return resourcePath;
  }

  public void setResourcePath(String resourcePath) {
    this.resourcePath = resourcePath;
  }

  public SpecificEvent getSpecificEvent() {
    return specificEvent;
  }

  public void setSpecificEvent(SpecificEvent specificEvent) {
    this.specificEvent = specificEvent;
  }
}
