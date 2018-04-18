package org.sims.discovery.models;

import java.util.List;

public class Service extends ServiceAdapter{
  
  private String id;
  private String name;
  private List<IRelatedParty> relatedParty;
  

  
  public Service(String name, List<IRelatedParty> relatedParty){
    this.name = name;
    this.relatedParty = relatedParty;
  }

  public Service(String id, IService service){
    this(service.getName(), service.getRelatedParty());
    this.id = id;
  }


  @Override
  public String getName(){
    return this.name;
  }

  @Override
  public String getId(){
    return this.id;
  }

  @Override
  public List<IRelatedParty> getRelatedParty(){
    return this.relatedParty;
  }
}