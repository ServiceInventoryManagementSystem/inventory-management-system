package org.sims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
public class ServiceRelationship {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @ApiModelProperty(notes="")
  private Long id;

  @ApiModelProperty(notes="")
  private String type;

  //TODO find a better name
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