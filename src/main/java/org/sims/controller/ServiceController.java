package org.sims.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.github.fge.jsonpatch.JsonPatch;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.apache.commons.beanutils.MethodUtils;
import org.sims.model.*;
import org.sims.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;


@RestController
@CrossOrigin("*")
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

  //TODO Return proper message back when resource isn't found (if(user==null))
  //Returns the service resource of the given id
  @GetMapping("/service/{id}")
  @ResponseBody
  public MappingJacksonValue getService(@PathVariable Long id, @RequestParam MultiValueMap<String,
          String> params, @QuerydslPredicate(root = Service.class) Predicate predicate) {

    QService qService = QService.service;
    Predicate p = new BooleanBuilder(predicate);
    ((BooleanBuilder) p).and(qService.id.eq(id));
    Optional<Service> service = serviceRepository.findOne(p);
    if(!service.isPresent()) {
      return new MappingJacksonValue("No service with parameters: " + p.toString());
    }
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


  @PatchMapping("/service/{id}")
  @Transactional
  @ResponseStatus(HttpStatus.CREATED)
  public MappingJacksonValue patchService(@PathVariable("id") Long id, @RequestBody JsonPatch jsonPatch) {
    ObjectMapper objectMapper;

    return new MappingJacksonValue("");
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




  @GetMapping("/seed/{count}")
  @Transactional
  @ResponseStatus(HttpStatus.CREATED)
  public void seed(@PathVariable("count") int count) {
    if (count > 50) {
      count = 50;
    }
    Random random = new Random();

    String[] categoryArray = {"CFS", "RFS"};
    String[] nameArray = {"serviceName1", "serviceName2", "serviceName3", "serviceName4", "serviceName5"};
    String[] descriptionArray = {};
    String[] hrefArray = {"http://server:port/serviceInventory/service/id"};
    Boolean[] booleanArray = {true, false};
    String[] startModeArray = {"0", "1", "2", "3", "4", "5", "6"};
    String[] stateArray = {"active", "inactive", "stopped"};
    String[] typeArray = {"type1", "type2", "type3"};

    ServiceSpecification[] serviceSpecificationArray = new ServiceSpecification[count];
    for (int i = 0; i < count; i++) {
      ServiceSpecification serviceSpecification = new ServiceSpecification();
      serviceSpecification.setHref("test");
      serviceSpecification.setName("name");
      serviceSpecificationRepository.save(serviceSpecification);
      serviceSpecificationArray[i] = serviceSpecification;
    }

    for (int j = 0; j < count; j++) {
      Service service = new Service();
      service.setName(nameArray[random.nextInt(nameArray.length)]);
      service.setCategory(categoryArray[random.nextInt(categoryArray.length)]);
      service.setDescription("empty");
      service.setHref(hrefArray[random.nextInt(hrefArray.length)]);
      service.setHasStarted(booleanArray[random.nextInt(2)]);
      service.setIsServiceEnabled(booleanArray[random.nextInt(2)]);
      service.setIsStateful(booleanArray[random.nextInt(2)]);
      service.setStartMode(startModeArray[random.nextInt(startModeArray.length)]);
      service.setState(stateArray[random.nextInt(stateArray.length)]);
      service.setType(typeArray[random.nextInt(typeArray.length)]);
      service.setServiceSpecification(serviceSpecificationArray[j]);
      serviceRepository.save(service);
    }
  }



  //TODO
  //Started different patch
  /*
  //TODO Check if the requestbody is a PatchObject or a Service
  @PatchMapping("/service/{id}")
  @Transactional
  @ResponseStatus(HttpStatus.CREATED)
  public MappingJacksonValue patchService(@PathVariable("id") Long id, @Valid @RequestBody PatchObject patchObject) {
    System.out.println("Entered patch");
    Optional<Service> optionalService = serviceRepository.findById(id);
    System.out.println("Got service object");
    if (!optionalService.isPresent()) {
      //TODO proper response
      return new MappingJacksonValue("Returns null");
    }
    Service service = optionalService.get();

    System.out.println("Service name = " + service.getName());
    System.out.println("Path = " + patchObject.getPath());
    System.out.println("Value = " + patchObject.getValue());
    System.out.println(patchObject.getValue().getClass());


    switch (patchObject.getPath()) {
      case "Note":
        noteRepository.save(new Note());
        break;
      case "Place":
        placeRepository.save(new Place());
        break;
      case "RelatedParty":
        relatedPartyRepository.save(new RelatedParty());
        break;
      case "ServiceCharacteristic":
        serviceCharacteristicRepository.save(new ServiceCharacteristic());
        break;
      case "ServiceOrder":
        serviceOrderRepository.save(new ServiceOrder());
        break;
      case "ServiceRef":
        serviceRefRepository.save(new ServiceRef());
        break;
      case "ServiceRelationship":
        serviceRelationshipRepository.save(new ServiceRelationship());
        break;
      case "ServiceSpecification":
        //TODO update with id
        System.out.println("Entered servicespecification");
        System.out.println("patchObject.getValue() = " + patchObject.getValue());
        if (patchObject.getOp().equals("update")) {
          QServiceSpecification qServiceSpecification = QServiceSpecification.serviceSpecification;
          Predicate predicate = new BooleanBuilder();
          ((BooleanBuilder) predicate).and(qServiceSpecification.service.id.eq(id));
          Optional<ServiceSpecification> optionalServiceSpecification = serviceSpecificationRepository.findOne(predicate);
          if (!optionalServiceSpecification.isPresent()) {
            return new MappingJacksonValue("No servicespecification found for that id");
          }
          ServiceSpecification serviceSpecification = optionalServiceSpecification.get();
          LinkedHashMap<String, String> linkedHashMap = patchObject.getValue() instanceof LinkedHashMap ? ((LinkedHashMap) patchObject.getValue()) : null;
          if (linkedHashMap == null) {
            return new MappingJacksonValue("Invalid value");
          }
          for (String key : linkedHashMap.keySet()) {
            try {
              System.out.println("Entered try block");
              MethodUtils.invokeMethod(serviceSpecification, "set" + StringUtils.capitalize(key), linkedHashMap.get(key));
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
              e.printStackTrace();
            }
          }

          serviceSpecificationRepository.save(serviceSpecification);
        }
        else if (patchObject.getOp().equals("replace")) {
          ServiceSpecification serviceSpecification = new ServiceSpecification();
          LinkedHashMap<String, String> linkedHashMap = patchObject.getValue() instanceof LinkedHashMap ? ((LinkedHashMap) patchObject.getValue()) : null;
          if (linkedHashMap == null) {
            return new MappingJacksonValue("Invalid value");
          }
          for (String key : linkedHashMap.keySet()) {
            try {
              MethodUtils.invokeMethod(serviceSpecification, "set" + StringUtils.capitalize(key), linkedHashMap.get(key));
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
              e.printStackTrace();
            }
          }
          service.setServiceSpecification(serviceSpecification);
          serviceSpecificationRepository.save(serviceSpecification);
        }

        //----------------------------------------------Experimental----------------------------------------------------
        //TODO Error: More than one row with the given identifier was found
        else if (patchObject.getOp().equals("changeid")) {
          System.out.println("Entered changeid");
          String temp = patchObject.getValue().toString();
          System.out.println("Got value");
          Long bleb = Long.parseLong(temp);
          System.out.println("converted to long");
          Optional<ServiceSpecification> optionalServiceSpecification = serviceSpecificationRepository.findById(bleb);
          System.out.println("Found by id");
          ServiceSpecification serviceSpecification = optionalServiceSpecification.get();
          System.out.println("Got from optional");

          service.setServiceSpecification(serviceSpecification);
          System.out.println("set the servicespecification");
          serviceRepository.save(service);
          System.out.println("saved the service");
        }
        break;
      case "SupportingResource":
        supportingResourceRepository.save(new SupportingResource());
        break;
      case "SupportingService":
        supportingServiceRepository.save(new SupportingService());
    }

    try {
      System.out.println("Trying set");
      MethodUtils.invokeExactMethod(service, "set" + patchObject.getPath(), patchObject.getValue());
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      System.err.println(e);
    }

    return new MappingJacksonValue("Returns Service");

  }*/
  /*
  //TODO Exception handling for invalid id
  //TODO Be able to create subresources
  //TODO add check for update or delete
  //Partially updates a service according to the TMForum API
  @PatchMapping("/service/{id}")
  @Transactional
  @ResponseStatus(HttpStatus.CREATED)
  public MappingJacksonValue patchService(@PathVariable("id") Long id, @Valid @RequestBody PatchObject patchObject) {
    Optional<Service> s = this.serviceRepository.findById(id);
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
    args[1] = patchObject.getOp();
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
  }*/
}
