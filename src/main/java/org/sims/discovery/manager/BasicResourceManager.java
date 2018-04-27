package org.sims.discovery.manager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.sims.discovery.models.IService;

import io.reactivex.Completable;
import io.reactivex.Single;


public abstract class BasicResourceManager implements IResourceManager{
  // Map localref to id
  private HashMap<String, String> serviceMap;
  private HashMap<String, String> reverseMap = new HashMap<String, String>();

  private ObjectMapper objectMapper;
  private ObjectWriter objectWriter;
  
  
  public BasicResourceManager(){
    objectMapper = new ObjectMapper();
    
    objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
    ObjectReader objectReader = objectMapper.readerFor(HashMap.class);

    try{
      serviceMap = objectReader.readValue("./data/serviceref.json");
      for(String localRef : serviceMap.keySet()){
        String remoteId = serviceMap.get(localRef);
        put(localRef, remoteId);
      }
    }catch(IOException e){
      serviceMap = new HashMap<String, String>();
      flush();
    }
  }

  @Override
  public Single<String> save(IService service){
    put(service.getLocalReference(), service.getId());
    flush();
    return Single.just(service.getId());
  }

  public Completable removeService(String id){
    remove(id);
    flush();
    return Completable.complete();
  }

  private void put(String localRef, String remoteId){
    //if(!serviceMap.containsKey(localRef)){
      serviceMap.putIfAbsent(localRef, remoteId);
      reverseMap.putIfAbsent(remoteId, localRef);
    //}
  }

  private void remove(String remoteId){
    System.out.println(reverseMap.containsKey(remoteId));
    if(reverseMap.containsKey(remoteId)){
      String localRef = reverseMap.get(remoteId);
      serviceMap.remove(localRef);
      reverseMap.remove(remoteId);
      System.out.println("Removed service from map " + remoteId + " " + localRef);
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

  @Override
  public void dispose(){
    flush();
  }
}