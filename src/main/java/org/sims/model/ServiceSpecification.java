package org.sims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.jdo.annotations.Unique;
import javax.persistence.*;
import java.io.Serializable;

@Entity
public class ServiceSpecification implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ApiModelProperty(notes="Unique identifier of the service specification.")
  private String jsonId;


  @ApiModelProperty(notes="Reference of the ServiceSpecification.")
  private String href;
  @ApiModelProperty(notes="Name of the required ServiceSpecification. ")
  private String name;
  @ApiModelProperty(notes="Service specification version.")
  private String version;

  @OneToOne(fetch = FetchType.LAZY, mappedBy = "serviceSpecification")
  private Service service;

  @JsonIgnore
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getHref() {
    return href;
  }

  public void setHref(String href) {
    this.href = href;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  @JsonIgnore
  public Service getService() {
    return service;
  }

  public void setService(Service service) {
    this.service = service;
  }

  @JsonProperty("id")
  public String getJsonId() {
    return jsonId;
  }

  @JsonProperty("id")
  public void setJsonId(String jsonId) {
    this.jsonId = jsonId;
  }
}
