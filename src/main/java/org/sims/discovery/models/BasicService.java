package org.sims.discovery.models;

import java.util.List;

public class BasicService extends ServiceAdapter{
  
  private String id;
  private String name;
  private List<IRelatedParty> relatedParty;
  private final String ref;
  
  public BasicService(String id, String name, String ref, List<IRelatedParty> relatedParty){
    this.name = name;
    this.ref = ref;
    this.relatedParty = relatedParty;
    this.id = id;
  }

  public BasicService(IService service){
    this(service.getId(), service.getName(), service.getLocalReference(), service.getRelatedParty());
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