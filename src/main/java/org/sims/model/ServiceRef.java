package org.sims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class ServiceRef {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String href;

  @OneToOne(mappedBy = "serviceRef")
  private ServiceRelationship serviceRelationship;

  public Long getId() {
    return id;
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
}
