package org.sims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class ServiceRelationship {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long dbid;

  private String type;

  @ManyToOne
  private Service service;

  @OneToOne(cascade = CascadeType.ALL)
  private ServiceRef serviceRef;

  @JsonIgnore
  public Long getDbid() {
    return dbid;
  }

  public void setDbid(Long dbid) {
    this.dbid = dbid;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @JsonIgnore
  public Service getService() {
    return service;
  }

  public void setService(Service service) {
    this.service = service;
  }

  public ServiceRef getServiceRef() {
    return serviceRef;
  }

  public void setServiceRef(ServiceRef serviceRef) {
    this.serviceRef = serviceRef;
  }
}