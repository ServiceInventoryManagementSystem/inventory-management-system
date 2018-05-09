package org.sims.model;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
public class ServiceSpecification {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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
  public void setHref() {
    this.href = null;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  public void setName() {
    this.name = null;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }
  public void setVersion() {
    this.version = null;
  }

}
