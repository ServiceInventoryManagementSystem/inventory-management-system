package org.sims.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ServiceRelationship implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String type;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonBackReference
  @JoinColumn(name = "owning_service_id")
  private Service owningService;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  private ServiceRef service;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @JsonIgnore
  public Service getOwningService() {
    return owningService;
  }

  public void setOwningService(Service owningService) {
    this.owningService = owningService;
  }

  public ServiceRef getService() {
    return service;
  }

  public void setServiceRef(ServiceRef service) {
    this.service = service;
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