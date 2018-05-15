package org.sims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
public class ServiceRef {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long dbid;

  @ApiModelProperty(notes="Id of the service.")
  private String id;

  @ApiModelProperty(notes="reference of the service.")
  private String href;

  @OneToOne(mappedBy = "serviceRef")
  private ServiceRelationship serviceRelationship;

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

  @JsonIgnore
  public ServiceRelationship getServiceRelationship() {
    return serviceRelationship;
  }

  public void setServiceRelationship(ServiceRelationship serviceRelationship) {
    this.serviceRelationship = serviceRelationship;
  }

//  public Long getDbid() {
//    return dbid;
//  }
//
//  public void setDbid(Long dbid) {
//    this.dbid = dbid;
//  }
}
