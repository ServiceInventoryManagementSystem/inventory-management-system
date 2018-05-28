package org.sims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class SupportingService implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String jsonId;

  private String category;
  private String href;
  private String name;

  @ManyToMany(mappedBy = "supportingServices")
  private List<Service> services = new ArrayList<>();

  @JsonIgnore
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
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

  @JsonIgnore
  public List<Service> getServices() {
    return services;
  }

  public void setServices(List<Service> services) {
    this.services = services;
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
