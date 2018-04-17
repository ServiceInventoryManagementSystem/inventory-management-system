package org.sims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
public class ServiceRef {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @ApiModelProperty(notes="Id of the service.")
  private Long id;

  @ApiModelProperty(notes="reference of the service.")
  private String href;

  @OneToOne(mappedBy = "service")
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
