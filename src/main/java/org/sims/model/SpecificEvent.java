package org.sims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class SpecificEvent {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;

  @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST})
  private Service service;

  @ManyToOne
  @JoinColumn(name = "specific_resource_id")
  private SpecificResource specificResource;

  @OneToMany(mappedBy = "specificEvent", cascade = CascadeType.ALL)
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
