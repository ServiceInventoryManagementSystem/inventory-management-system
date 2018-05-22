package org.sims.discovery.manager;

import java.awt.image.ImagingOpException;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.IIOException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Basic;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.sun.xml.messaging.saaj.util.Base64;

import org.sims.discovery.models.BasicService;
import org.sims.discovery.models.IHasId;
import org.sims.discovery.models.IRelatedParty;
import org.sims.discovery.models.IService;
import org.sims.discovery.models.ServiceWrapper;
import org.sims.model.RelatedParty;
import org.sims.model.Service;
import org.sims.repository.ServiceRepository;
import org.sims.utils.MagicWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class HybernateResourceManager extends BasicResourceManager{
  @Autowired
  ServiceRepository serviceRepo;


  public HybernateResourceManager(){
    super();
  }
 
  private Example<Service> getExample(String id){
    Service exampleService = new Service();
    exampleService.setId(id);
    return Example.of(exampleService);
  }

  public Single<IService> save(IService service){
    Service model = new ServiceMapper(service).getService();
    String remoteId = service.getId();

    if(service.getId() == null){
      remoteId = super.getRemoteId(service.getLocalReference());
    }
    
    if(remoteId == null) {
    } else {
      model = MagicWrapper.createProxy(Service.class, true, new Service[]{model, serviceRepo.findOne(getExample(remoteId)).get()});  
    }

    serviceRepo.save(model);
    final String id = "" + serviceRepo.save(model).getId();
    
    IHasId serviceId = new IHasId(){
      @Override
      public String getId() {
        return id;
      }
    };

    return super.save(MagicWrapper.createProxy(IService.class, true, new Object[]{serviceId, service} ));
    //return super.save(ServiceWrapper.combine((IService)serviceId, service));
  }

  public Single<IService> getById(String id){
    return null;
  }

  public Single<List<IService>> getOwnedServices(){
    return null;
  }

  public Single<List<IService>> getByIds(String... ids){
    return null;
  }

  public Completable removeService(String id){
    serviceRepo.delete(serviceRepo.findOne(getExample(id)).get());
    return super.removeService(id);
  }

  public Completable removeService(IService service){
    String remoteId = service.getId();
    if(remoteId == null){
      remoteId = super.getRemoteId(service.getLocalReference());
    }

    return this.removeService(remoteId);
  }
  
  public void stopWatching(IService service){
    String remoteId = service.getId();
    if(remoteId == null){
      remoteId = super.getRemoteId(service.getLocalReference());
    }
    this.stopWatching(remoteId);
  }

  public void stopWatching(String id){
    super.stopWatching(id);
  }


  public class ServiceMapper{
    private IService service;
    public ServiceMapper(IService service){
      this.service = service;
    }

    public Service getService(){
      Service model = new Service();
      model.setName(service.getName());
      model.setCategory(service.getCategory());
      model.setDescription(service.getDescription());
      //model.setEndDate(service.getEndDate().toGMTString());
      //model.setStartDate(service.getStartDate().toGMTString());
      model.setStateful(service.isStateful());
      model.setServiceEnabled(service.isServiceEnabled());
      model.setHasStarted(service.hasStarted());
      model.setState(service.getState());
      model.setCategory(service.getCategory());
      model.setHref(service.getHref());

      List<RelatedParty> relatedParties = new ArrayList<RelatedParty>();
      for(IRelatedParty party : service.getRelatedParty()){
        RelatedParty partyModel = new RelatedParty();
        //partyModel.set
        relatedParties.add(partyModel);
      }

      return model;
    }


  }
}

/*

  //Service was discovered
  private String addService(IService s){
    
    Example<Service> example = Example.of(new Service());
    Service entry = serviceRepo.findOne(s.getId());

    //Service entry = new Service();
    if(entry == null){
      System.out.println("Service does not already exist creating");
      entry = new Service();
      // Map IService to Service, should be moved to helper method
      entry.setUuid(UUID);
      entry.setHasStarted(s.hasStarted());
      entry.setDescription(s.getDescription());
      entry.setCategory("MANAGED");
    }
    entry.setName(s.getName());
    entry.setHref(s.getHref());
      
    return serviceRepo.save(entry).getId().toString();
    
  }

  private void updateService(IService s){

    
    //Service entry = new Service();
    if(entry == null){
      System.out.println("Service does not already exist creating");
      entry = new Service();
      // Map IService to Service, should be moved to helper method
      entry.setUuid(UUID);
      entry.setHasStarted(s.hasStarted());
      entry.setDescription(s.getDescription());
      entry.setCategory("MANAGED");
    }
    entry.setName(s.getName());
    entry.setHref(s.getHref());
      
    return serviceRepo.save(entry).getId().toString();
  }

  //Service was lost
  private void removeService(IService s){
    String id = s.getId();
    Service example = new Service();
    example.setUuid(UUID);

    Service res = serviceRepo.getByUuid(UUID);
    if(res == null){
      System.out.println("Service remove: was not tracked");
    } else {
      // Set service state to 'terminated'
      System.out.println("Service remove: state set to terminated");
      res.setState("terminated");
      serviceRepo.save(res);
    }
  }
 */