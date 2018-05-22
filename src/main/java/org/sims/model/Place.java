package org.sims.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;


@Entity
public class Place {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ApiModelProperty(notes="Referance of a place (for instance in google map).")
  private String href;

  @ApiModelProperty(notes="Role of the place (for instance delivery geographical place.)")
  private String role;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonBackReference
  @JoinColumn(name = "service_id")
  private Service service;

  public Place() {
  }

  @JsonIgnore
  public Service getService() {
    return service;
  }

  public void setService(Service service) {
    this.service = service;
  }

  public String getHref() {
    return href;
  }

  public void setHref(String href) {
    this.href = href;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  @JsonIgnore
  public Long getId() {
    return id;
  }

  @JsonIgnore
  public void setId(Long id) {
    this.id = id;
  }
}