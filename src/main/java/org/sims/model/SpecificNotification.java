package org.sims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
public class SpecificNotification {
  @JsonProperty("eventId")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;

  @CreationTimestamp
  private OffsetDateTime eventTime;
  private String eventType;

  @Transient
  private Event event;

  @Transient
  @JsonProperty("event")
  private SpecificEvent specificEvent;

  public OffsetDateTime getEventTime() {
    return eventTime;
  }

  public void setEventTime(OffsetDateTime eventTime) {
    this.eventTime = eventTime;
  }

  public String getEventType() {
    return eventType;
  }

  public void setEventType(String eventType) {
    this.eventType = eventType;
  }

  public SpecificEvent getSpecificEvent() {
    return specificEvent;
  }

  public void setSpecificEvent(SpecificEvent specificEvent) {
    this.specificEvent = specificEvent;
  }

  @JsonIgnore
  public Event getEvent() {
    return event;
  }

  public void setEvent(Event event) {
    this.event = event;
  }
}

