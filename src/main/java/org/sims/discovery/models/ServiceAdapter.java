package org.sims.discovery.models;

import java.util.Date;

public abstract class ServiceAdapter implements IService{
  
  public String getId(){return null;}
  public String getHref(){return null;}
  public String getDescription(){return null;}
  public String getName(){return null;}
  public String getStartMode(){return null;}
  public String getState(){return null;}
  public String getType(){return null;}
  public String getCategory(){return null;}

  public Date getDiscoveryDate(){return null;}
  public Date getStartDate(){return null;}
  public Date getEndDate(){return null;}
  public Date getOrderDate(){return null;}

  public Boolean isServiceEnabled(){return null;}
  public Boolean isStateful(){return null;}
  public Boolean hasStarted(){return null;}

  public int compareTo(IService other){
    return 0;
  }

}