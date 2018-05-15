package org.sims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
public class ServiceRelationship {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String type;

  @ManyToOne
  private Service service;

  @OneToOne(cascade = CascadeType.ALL)
  private ServiceRef serviceRef;

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

  @JsonProperty("service")
  public ServiceRef getServiceRef() {
    return serviceRef;
  }

  public void setServiceRef(ServiceRef serviceRef) {
    this.serviceRef = serviceRef;
  }

//  public Long getId() {
//    return id;
//  }
//
//  public void setId(Long id) {
//    this.id = id;
//  }
}