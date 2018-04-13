package org.sims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.jws.HandlerChain;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class RelatedParty {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String href;
  private String name;
  private String role;
  private String validFor;


  @ManyToMany(mappedBy = "relatedParties") //
  private Set<Service> services = new HashSet<>();

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

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getValidFor() {
    return validFor;
  }

  public void setValidFor(String validFor) {
    this.validFor = validFor;
  }

  @JsonIgnore
  public Set<Service> getServices() {
    return services;
  }

  public void setServices(Set<Service> services) {
    this.services = services;
  }
}
