package org.sims.discovery.mdns;

import java.util.Date;

import javax.jmdns.ServiceInfo;

import org.sims.discovery.models.IService;;

public class DnsService implements IService{
  private ServiceInfo info;
  private Date discoveryDate;

  public DnsService(ServiceInfo info){
    this.info = info;
  }


  public String getUUID(){
    return String.format("dns:%s",info.getKey());

  }

  public String getHref(){
    return info.getPropertyString("path");
  }

  public String getName(){
    return info.getName();
  }

  public String getDescription(){
    return "mDNS - service";
  }

  public Boolean hasStarted(){
    return true;
  }

  public Date getDiscovered(){
    return this.discoveryDate;
  }

}