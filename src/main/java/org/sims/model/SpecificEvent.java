package org.sims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Component
public class SpecificEvent {

  private Service service;
  private Set<SpecificNotification> specificNotifications = new HashSet<>();

  public Service getService() {
    return service;
  }

  public void setService(Service service) {
    this.service = service;
  }

  @JsonIgnore
  public Set<SpecificNotification> getSpecificNotifications() {
    return specificNotifications;
  }

  public void setSpecificNotifications(Set<SpecificNotification> specificNotifications) {
    this.specificNotifications = specificNotifications;
  }
}
