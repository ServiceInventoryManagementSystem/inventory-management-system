package org.sims.controller;


import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.querydsl.core.types.Predicate;
import org.apache.commons.beanutils.MethodUtils;
import org.sims.model.Place;
import org.sims.model.RelatedParty;
import org.sims.model.Service;
import org.sims.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class ServiceController implements Serializable {

  private final ServiceRepository serviceRepository;
  private final NoteRepository noteRepository;
  private final PlaceRepository placeRepository;
  private final RelatedPartyRepository relatedPartyRepository;
  private final ServiceCharacteristicRepository serviceCharacteristicRepository;
  private final ServiceOrderRepository serviceOrderRepository;
  private final ServiceRefRepository serviceRefRepository;
  private final ServiceRelationshipRepository serviceRelationshipRepository;
  private final ServiceSpecificationRepository serviceSpecificationRepository;
  private final SupportingResourceRepository supportingResourceRepository;
  private final SupportingServiceRepository supportingServiceRepository;


  //TODO Make'em patch
  @Autowired
  public ServiceController(ServiceRepository serviceRepository, NoteRepository noteRepository,
                           PlaceRepository placeRepository, RelatedPartyRepository relatedPartyRepository,
                           ServiceCharacteristicRepository serviceCharacteristicRepository,
                           ServiceOrderRepository serviceOrderRepository, ServiceRefRepository serviceRefRepository,
                           ServiceRelationshipRepository serviceRelationshipRepository,
                           ServiceSpecificationRepository serviceSpecificationRepository,
                           SupportingResourceRepository supportingResourceRepository,
                           SupportingServiceRepository supportingServiceRepository) {
    this.serviceRepository = serviceRepository;
    this.noteRepository = noteRepository;
    this.placeRepository = placeRepository;
    this.relatedPartyRepository = relatedPartyRepository;
    this.serviceCharacteristicRepository = serviceCharacteristicRepository;
    this.serviceOrderRepository = serviceOrderRepository;
    this.serviceRefRepository = serviceRefRepository;
    this.serviceRelationshipRepository = serviceRelationshipRepository;
    this.serviceSpecificationRepository = serviceSpecificationRepository;
    this.supportingResourceRepository = supportingResourceRepository;
    this.supportingServiceRepository = supportingServiceRepository;
  }

  //Method to return only the specified fields
  private MappingJacksonValue applyFieldFiltering(MappingJacksonValue mappingJacksonValue, MultiValueMap<String,
          String> params) {
    SimpleFilterProvider filters;
    if (params.containsKey("fields")) {
      filters = (new SimpleFilterProvider()).addFilter("org.sims.model.Service",
              SimpleBeanPropertyFilter.filterOutAllExcept((params.getFirst("fields")).split(",")));
      mappingJacksonValue.setFilters(filters);
      return mappingJacksonValue;
    } else {
      filters = (new SimpleFilterProvider()).addFilter("org.sims.model.Service", SimpleBeanPropertyFilter.serializeAll());
      mappingJacksonValue.setFilters(filters);
      return mappingJacksonValue;
    }
  }

  //Get all services. If "fields" is present, only the fields specified will be returned.
  @GetMapping("/service")
  @ResponseBody
  public MappingJacksonValue getServices(@RequestParam MultiValueMap<String,
          String> params, @QuerydslPredicate(root = Service.class) Predicate predicate) {
    Iterable<Service> services = this.serviceRepository.findAll(predicate);
    MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(services);
    return applyFieldFiltering(mappingJacksonValue, params);
  }

  //TODO Apply filtering, for some reason
  //TODO Return proper message back when resource isn't found (if(user==null))
  //Returns the service resource of the given id
  @GetMapping("/service/{id}")
  @ResponseBody
  public MappingJacksonValue getService(@PathVariable Long id, @RequestParam MultiValueMap<String,
          String> params, @QuerydslPredicate(root = Service.class) Predicate predicate) {
    Optional<Service> service = this.serviceRepository.findById(id);
    MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(service);
    return applyFieldFiltering(mappingJacksonValue, params);

  }

  //TODO currently working, but need to find a better way to return the created object
  //Creates a service in the database from the service JSON passed. Returns the created object.
  @PostMapping("/service")
  @Transactional
  @ResponseStatus(HttpStatus.CREATED)
  public MappingJacksonValue createService(@Valid @RequestBody Service service) {
    SimpleFilterProvider filters;
    filters = (new SimpleFilterProvider()).addFilter("org.sims.model.Service",
            SimpleBeanPropertyFilter.serializeAll());
    MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(serviceRepository.save(service));
    mappingJacksonValue.setFilters(filters);
    return mappingJacksonValue;
  }

  //TODO Proper exception handling for invalid id
  //Deletes the service at the given id
  @DeleteMapping("/service/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteService(@PathVariable Long id) {
    serviceRepository.deleteById(id);
  }

  //Deletes all services in the database
  @DeleteMapping("/service")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteServices() {
    serviceRepository.deleteAll();
  }

  //TODO Exception handling for invalid id
  //TODO Be able to create subresources
  //TODO add check for update or delete
  //Partially updates a service according to the TMForum API
  @PatchMapping("/service/{id}")
  @Transactional
  @ResponseStatus(HttpStatus.CREATED)
  public MappingJacksonValue patchService(@PathVariable("id") Long id, @Valid @RequestBody PatchObject patchObject) {
    Optional<Service> s = this.serviceRepository.findById(id);
    System.out.println("ServiceRepository inside the service Controller = " + serviceRepository);
    if (!s.isPresent()) {
      //TODO add proper exception handling and http status code
      return null;
    }
    Service service = s.get();

    System.out.println("Service name = " + service.getName());
    System.out.println("Path = " + patchObject.getPath());
    System.out.println("Value = " + patchObject.getValue());
    System.out.println(patchObject.getValue().getClass());
    Object[] args = new Object[2];
    args[0] = patchObject.getValue();
    args[1] = patchObject.getOp().toString();
    try {
      System.out.println("Trying customSet");
      MethodUtils.invokeExactMethod(service, "customSet" + patchObject.getPath(), args);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }

    try {
      System.out.println("Trying set");
      MethodUtils.invokeExactMethod(service, "set" + patchObject.getPath(), patchObject.getValue());
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      System.err.println(e);
    }

    serviceRepository.save(service);
    SimpleFilterProvider filters;
    filters = (new SimpleFilterProvider()).addFilter("org.sims.model.Service",
            SimpleBeanPropertyFilter.serializeAll());
    MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(service);
    mappingJacksonValue.setFilters(filters);
    return mappingJacksonValue;
  }
}
