package org.sims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SpecificNotification implements Serializable {
  @JsonProperty("eventId")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;

//  @CreationTimestamp
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

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}

