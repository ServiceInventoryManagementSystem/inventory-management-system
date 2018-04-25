package org.sims.discovery.manager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.sims.discovery.models.IService;

import io.reactivex.Single;


public abstract class BasicResourceManager implements IResourceManager{
  // Map localref to id
  private HashMap<String, String> serviceMap;

  private ObjectMapper objectMapper;
  private ObjectWriter objectWriter;
  
  
  public BasicResourceManager(){
    objectMapper = new ObjectMapper();
    
    objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
    ObjectReader objectReader = objectMapper.readerFor(HashMap.class);

    try{
      serviceMap = objectReader.readValue("./data/serviceref.json");
    }catch(IOException e){
      serviceMap = new HashMap<String, String>();
      flush();
    }
  }

  @Override
  public Single<String> save(IService service){
    if(!serviceMap.containsKey(service.getLocalReference())){
      serviceMap.put(service.getLocalReference(), service.getId());
    }
    return Single.just(service.getId());
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