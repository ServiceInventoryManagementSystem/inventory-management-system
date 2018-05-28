package org.sims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class ServiceRef implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ApiModelProperty(notes="Id of the service.")
  private String jsonId;

  @ApiModelProperty(notes="reference of the service.")
  private String href;

  @OneToOne(mappedBy = "service")
  private ServiceRelationship serviceRelationship;

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

  @JsonIgnore
  public ServiceRelationship getServiceRelationship() {
    return serviceRelationship;
  }

  public void setServiceRelationship(ServiceRelationship serviceRelationship) {
    this.serviceRelationship = serviceRelationship;
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
