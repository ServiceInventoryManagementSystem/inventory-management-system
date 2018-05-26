package org.sims.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;


@Entity
public class ServiceCharacteristic implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ApiModelProperty(notes="Name of the characteristic.")
  private String name;
  @ApiModelProperty(notes="Value of the characteristic.")
  private String value;

  @ManyToOne
  @JsonBackReference
  @JoinColumn(name="service_id")
  private Service service;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @JsonIgnore
  public Service getService() {
    return service;
  }

  public void setService(Service service) {
    this.service = service;
  }

  @JsonIgnore
  public Long getId() {
    return id;
  }

//  @JsonIgnore
  public void setId(Long id) {
    this.id = id;
  }
}