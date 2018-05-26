package org.sims.discovery.models;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

public interface IService extends IHasId{

  @NotNull
  public String getName();

  @NotEmpty
  public List<IRelatedParty> getRelatedParty();

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