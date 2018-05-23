package org.sims.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.sims.model.*;
import org.sims.repository.HubRepository;
//import org.sims.repository.SpecificNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class HubController {

  private HubRepository hubRepository;
//  private SpecificNotificationRepository specificNotificationRepository;

  @Autowired
  public HubController(HubRepository hubRepository/*, SpecificNotificationRepository specificNotificationRepository*/) {
    this.hubRepository = hubRepository;
//    this.specificNotificationRepository = specificNotificationRepository;
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/hub")
  public Hub registerListener(@RequestBody Hub hub) {
    return hubRepository.save(hub);
  }

  @GetMapping("/hub")
  public List<Hub> getHub() {
    return hubRepository.findAll();
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/hub/{id}")
  public void unregisterListener(@PathVariable String id) {
    QHub qHub = QHub.hub;
    Predicate p = new BooleanBuilder();
    ((BooleanBuilder) p).and(qHub.id.eq(id));
    Optional<Hub> optionalHub = hubRepository.findOne(p);
    if(!optionalHub.isPresent()) {
      throw new ResourceNotFoundException();
    }
    hubRepository.deleteById(id);
  }

}
