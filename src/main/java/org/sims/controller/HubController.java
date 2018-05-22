package org.sims.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.sims.model.Hub;
import org.sims.model.SpecificNotification;
import org.sims.repository.HubRepository;
import org.sims.repository.SpecificNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApiIgnore
@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class HubController {

  private HubRepository hubRepository;
  private SpecificNotificationRepository specificNotificationRepository;

  @Autowired
  public HubController(HubRepository hubRepository, SpecificNotificationRepository specificNotificationRepository) {
    this.hubRepository = hubRepository;
    this.specificNotificationRepository = specificNotificationRepository;
  }

  @PostMapping("/hub")
  public Hub registerListener(@RequestBody String requestBody) {

    MultiValueMap<String, String> callback = new LinkedMultiValueMap<>();
    if(requestBody != null) {
      callback.add("callback", requestBody);
    }

    System.out.println(requestBody);
    System.out.println(callback);

    ObjectMapper mapper = new ObjectMapper();

    TypeReference<HashMap<String, String>> typeRef
            = new TypeReference<HashMap<String, String>>() {};
    Map<String, String> map;
    try {
      map = mapper.readValue(requestBody, typeRef);
      System.out.println(map);
      System.out.println(map.get("callback"));
      Hub hub = new Hub();
      hub.setCallback(map.get("callback"));
      return hubRepository.save(hub);

    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;


  }

  @GetMapping("/hub")
  public MappingJacksonValue getHub() {
    MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(specificNotificationRepository.findAll());
    SimpleFilterProvider filters;
    filters = (new SimpleFilterProvider()).addFilter("service",
            SimpleBeanPropertyFilter.serializeAll());
    mappingJacksonValue.setFilters(filters);
    return mappingJacksonValue;
  }

  @DeleteMapping("/hub/{id}")
  public void unregisterListener(@PathVariable String id) {
    hubRepository.deleteById(id);
  }

}
