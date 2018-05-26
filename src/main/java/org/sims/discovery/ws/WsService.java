package org.sims.discovery.ws;

import com.ms.wsdiscovery.servicedirectory.WsDiscoveryService;
import org.sims.discovery.models.IRelatedParty;
import org.sims.discovery.models.ServiceAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

;



public class WsService extends ServiceAdapter{
  private WsDiscoveryService service;

  private String id;

  private String href;
  private String name = "Some ws discovery service";
  private String ref;

  private Date discoveredDate = new Date();

  private List<IRelatedParty> relatedParty = new ArrayList<IRelatedParty>();

  public WsService(WsDiscoveryService service){
    this.service = service;
    href = service.getXAddrs().get(0);
    ref = service.getEndpointReference().getAddress().toString();
    name = service.getPortTypes().get(0).toString();
  }


  @Override
  public Date getDiscoveryDate(){
    return discoveredDate;
  }

  @Override
  public String getHref(){
    return href;
  }

  @Override
  public String getName(){
    return name;
  }

  @Override
  public String getDescription(){
    return "WS-Discovery service";
  }

  @Override
  public Boolean hasStarted(){
    return true;
  }

  @Override
  public List<IRelatedParty> getRelatedParty(){
    return relatedParty;
  }

  @Override
  public String getLocalReference(){
    return "ws:" + ref;
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
    return "WS - RFS";
  }
}