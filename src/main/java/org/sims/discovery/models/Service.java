package org.sims.discovery.models;

import java.util.ArrayList;
import java.util.List;

public class Service extends ServiceAdapter{
  
  private String id;
  private String name;
  private List<IRelatedParty> relatedParty;
  private final String ref;
  
  public Service(String name, String ref, List<IRelatedParty> relatedParty){
    this.name = name;
    this.ref = ref;
    this.relatedParty = relatedParty;
  }

  public Service(String id, IService service){
    this(service.getName(), service.getLocalReference(), service.getRelatedParty());
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

  @Override
  public String getLocalReference(){
    return ref;
  }

}