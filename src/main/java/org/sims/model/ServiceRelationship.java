package org.sims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class ServiceRelationship {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String type;

  @ManyToOne
  private Service serviceRel;

  @OneToOne(cascade = CascadeType.ALL)
  private ServiceRef service;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

//  @JsonIgnore
//  public Service getService() {
//    return service;
//  }

  public void setServiceRel(Service serviceRel) {
    this.serviceRel = serviceRel;
  }

  public ServiceRef getService() {
    return service;
  }

  public void setService(ServiceRef service) {
    this.service = service;
  }
}