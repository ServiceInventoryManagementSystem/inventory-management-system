package org.sims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class ServiceSpecification {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String href;
  private String name;
  private String version;

  @OneToOne(fetch = FetchType.LAZY, mappedBy = "serviceSpecification")
  private Service service;

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
  public void setHref() {
    this.href = null;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  public void setName() {
    this.name = null;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }
  public void setVersion() {
    this.version = null;
  }

}
