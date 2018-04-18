package org.sims.discovery.models;

public class RelatedParty implements IRelatedParty{
  
  private String id;
  private String name;
  private String href;
  private String role;
  private String validFor;
  
  public RelatedParty(String name, String href, String role, String validFor){
    this.name = name;
    this.href = href;
    this.role = role;
    this.validFor = validFor;
  }

  public RelatedParty(String id, IRelatedParty relatedParty){
    this(relatedParty.getName(), relatedParty.getHref(), relatedParty.getRole(), relatedParty.getValidFor());
    this.id = id;
  }

  public String getId(){ return id; }
  public String getName(){ return name; }
  public String getHref(){ return href; }
  public String getRole(){ return role; }
  public String getValidFor(){ return validFor; }
}