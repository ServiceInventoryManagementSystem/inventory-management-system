package org.sims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class ServiceOrder {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long dbid;

  @ApiModelProperty(notes="Unique identifier of the related Service Order.")
  private String id;

  @ApiModelProperty(notes="The hyperlink to access the sleated Service Order.")
  private String href;

  @OneToMany(mappedBy = "serviceOrder")
  private Set<Service> services = new HashSet<>();

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
  public Set<Service> getServices() {
    return services;
  }

  public void setServices(Set<Service> services) {
    this.services = services;
  }

//  public String getDbid() {
//    return dbid;
//  }
//
//  public void setDbid(String dbid) {
//    this.dbid = dbid;
//  }
}