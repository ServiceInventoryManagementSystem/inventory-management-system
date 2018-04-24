package org.sims.discovery.manager;

import java.awt.image.ImagingOpException;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
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
import com.github.fge.jackson.JsonLoader;
import com.sun.xml.messaging.saaj.util.Base64;

import org.sims.discovery.models.IService;
import org.sims.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import io.reactivex.Observable;
import io.reactivex.Single;

public class HybernateResourceManager extends BasicResourceManager{
  @Autowired
  ServiceRepository serviceRepo;


  public HybernateResourceManager(){
    super();
  }

  public Single<IService> save(IService service){
    return null;
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