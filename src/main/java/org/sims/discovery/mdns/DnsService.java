package org.sims.discovery.mdns;

import org.sims.discovery.models.IRelatedParty;
import org.sims.discovery.models.ServiceAdapter;

import javax.jmdns.ServiceInfo;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DnsService extends ServiceAdapter{
  private ServiceInfo info;
  private Date discoveryDate;
  private List<IRelatedParty> relatedParty = new ArrayList<IRelatedParty>();

  public DnsService(ServiceInfo info){
    this.info = info;
  }

  @Override
  public String getHref(){
    return info.getURLs()[0];
  }

  @Override
  public String getName(){
    return info.getName();
  }

  @Override
  public String getDescription(){
    String url = "";
    if(info.getURLs().length > 0){
      url = info.getURLs()[0];
    }
    
    
    return String.format("Domain: %s\nUrl: %s\nKey: %s\nPath: %s\n Server: %s", info.getDomain(), url, info.getKey(), info.getPropertyString("path"), info.getServer());
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