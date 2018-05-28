package org.sims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class SpecificEvent implements Serializable {

  private Service service;
  private SpecificNotification specificNotification;

  public Service getService() {
    return service;
  }

  public void setService(Service service) {
    this.service = service;
  }

  @JsonIgnore
  public SpecificNotification getSpecificNotification() {
    return specificNotification;
  }

  public void setSpecificNotification(SpecificNotification specificNotification) {
    this.specificNotification = specificNotification;
  }

}
