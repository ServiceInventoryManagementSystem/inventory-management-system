package org.sims.discovery.models;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public interface IService {

  @NotNull
  public String getName();

  @NotEmpty
  public List<IRelatedParty> getRelatedParty();


  public String getId();
  public String getHref();
  public String getDescription();
  public String getStartMode();
  public String getState();
  public String getType();
  public String getCategory();
  
  public Date getDiscoveryDate();
  public Date getStartDate();
  public Date getEndDate();
  public Date getOrderDate();
  
  public Boolean isServiceEnabled();
  public Boolean isStateful();
  public Boolean hasStarted();

  // A local reference that can be used to map to id
  public String getLocalReference();
  
}