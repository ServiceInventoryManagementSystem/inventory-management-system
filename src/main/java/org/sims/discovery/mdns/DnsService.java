package org.sims.discovery.mdns;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jmdns.ServiceInfo;

import org.sims.discovery.models.IRelatedParty;
import org.sims.discovery.models.IService;
import org.sims.discovery.models.ServiceAdapter;


public class DnsService extends ServiceAdapter{
  private ServiceInfo info;
  private Date discoveryDate;
  private List<IRelatedParty> relatedParty = new ArrayList<IRelatedParty>();

  public DnsService(ServiceInfo info){
    this.info = info;
  }

  @Override
  public String getHref(){
    return info.getPropertyString("path");
  }

  @Override
  public String getName(){
    return info.getName();
  }

  @Override
  public String getDescription(){
    return "mDNS - service";
  }

  @Override
  public Boolean hasStarted(){
    return true;
  }

  @Override
  public Date getDiscoveryDate(){
    return this.discoveryDate;
  }
  
  @Override
  public String getLocalReference(){
    return String.format("dns:%s",info.getKey());
  }

  @Override
  public List<IRelatedParty> getRelatedParty(){
    return relatedParty;
  }

  @Override
  public String getState(){
    return "active";
  }

  @Override
  public Boolean isStateful(){
    return true;
  }

  @Override
  public String getCategory(){
    return "DNS - RFS";
  }

}