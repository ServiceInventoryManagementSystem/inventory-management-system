package org.sims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import javax.jdo.annotations.Unique;
import javax.persistence.*;

@Entity
public class ServiceSpecification {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long dbid;

  @ApiModelProperty(notes="Unique identifier of the service specification.")
  private String id;


  @ApiModelProperty(notes="Reference of the ServiceSpecification.")
  private String href;
  @ApiModelProperty(notes="Name of the required ServiceSpecification. ")
  private String name;
  @ApiModelProperty(notes="Service specification version.")
  private String version;

  @OneToOne(fetch = FetchType.LAZY, mappedBy = "serviceSpecification")
  private Service service;

  public String getId() {
    return id;
  }

  public void setId(String id) {
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

//  public Long getDbid() {
//    return dbid;
//  }
//
//  public void setDbid(Long dbid) {
//    this.dbid = dbid;
//  }
}
