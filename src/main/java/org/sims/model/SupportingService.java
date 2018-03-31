package org.sims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class SupportingService {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long dbid;

  private String category;
  private String href;
  private String id;
  private String name;

  @ManyToMany(mappedBy = "supportingServices")
  private List<Service> services = new ArrayList<>();

  @JsonIgnore
  public Long getDbid() {
    return dbid;
  }

  public void setDbid(Long dbid) {
    this.dbid = dbid;
  }

  public String getHref() {
    return href;
  }

  public void setHref(String href) {
    this.href = href;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @JsonIgnore
  public List<Service> getServices() {
    return services;
  }

  public void setServices(List<Service> services) {
    this.services = services;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }
}
