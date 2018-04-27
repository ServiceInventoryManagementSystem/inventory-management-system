package org.sims.discovery.ws;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.namespace.QName;

import com.ms.wsdiscovery.servicedirectory.WsDiscoveryService;

import org.sims.discovery.models.IRelatedParty;
import org.sims.discovery.models.BasicService;;
import org.sims.discovery.models.ServiceAdapter;
import org.sims.model.RelatedParty;



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
  }


  public Date getDiscovered(){
    return discoveredDate;
  }
    
  public String getHref(){
    return href;
  }

  public String getName(){
    return name;
  }

  public String getDescription(){
    return "WS-Discovery service";
  }

  public Boolean hasStarted(){
    return null;
  }

  public List<IRelatedParty> getRelatedParty(){
    return relatedParty;
  }

  public String getLocalReference(){
    return "ws:" + ref;
  }

}