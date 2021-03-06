package org.sims.discovery.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.reactivex.Completable;
import io.reactivex.Single;
import org.sims.discovery.models.IService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public abstract class BasicResourceManager implements IResourceManager{
  // Map localref to id
  private HashMap<String, String> serviceMap;
  private HashMap<String, String> reverseMap = new HashMap<String, String>();

  private ObjectMapper objectMapper;
  private ObjectWriter objectWriter;
  
  
  public BasicResourceManager(){
    open("./data/serviceref.json");
  }


  private void open(String file){
    objectMapper = new ObjectMapper();
    
    objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
    ObjectReader objectReader = objectMapper.readerFor(HashMap.class);

    try{
      serviceMap = objectReader.readValue(new File(file));
      for(String localRef : serviceMap.keySet()){
        String remoteId = serviceMap.get(localRef);
        put(localRef, remoteId);
      }
    }catch(IOException e){
      System.err.println(e);
      serviceMap = new HashMap<String, String>();
      flush();
    }

    
  }

  @Override
  public Single<IService> save(IService service){
    put(service.getLocalReference(), service.getId());
    flush();
    return Single.just(service);
  }

  public Completable removeService(String id){
    remove(id);
    flush();
    return Completable.complete();
  }

  private void put(String localRef, String remoteId){
    serviceMap.put(localRef, remoteId);
    reverseMap.put(remoteId, localRef);
  }

  private void remove(String remoteId){
    if(reverseMap.containsKey(remoteId)){
      String localRef = reverseMap.get(remoteId);
      serviceMap.remove(localRef);
      reverseMap.remove(remoteId);
      System.out.println("Removed service from map " + remoteId + " " + localRef);
      flush();
    }
  }

  public String getRemoteId(String localRef){
    return serviceMap.get(localRef);
  }

  private void flush(){
    try{
      File f = new File("./data/serviceref.json");
      f.getParentFile().mkdirs();
      objectWriter.writeValue(f, serviceMap);
      } catch (IOException e){
        System.err.println(e);
      }
  }

  public void stopWatching(IService service){
    this.stopWatching(service.getId());
  }
  public void stopWatching(String id){
    this.remove(id);
  }

  public List<String> getAllServiceIds(){
    return new ArrayList<String>(this.serviceMap.values());
  }

  @Override
  public void dispose(){
    flush();
  }
}