package org.sims.controller;


import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.github.fge.jsonpatch.JsonPatchException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.sims.controller.common.JsonMergePatcher;
import org.sims.controller.common.JsonPatcher;
import org.sims.model.*;
import org.sims.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;


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

  private JsonPatcher jsonPatcher;
  private JsonMergePatcher jsonMergePatcher;

  private SpecificNotificationRepository specificNotificationRepository;
  private SpecificEventRepository specificEventRepository;


  @Autowired
  public ServiceController(ServiceRepository serviceRepository, NoteRepository noteRepository,
                           PlaceRepository placeRepository, RelatedPartyRepository relatedPartyRepository,
                           ServiceCharacteristicRepository serviceCharacteristicRepository,
                           ServiceOrderRepository serviceOrderRepository, ServiceRefRepository serviceRefRepository,
                           ServiceRelationshipRepository serviceRelationshipRepository,
                           ServiceSpecificationRepository serviceSpecificationRepository,
                           SupportingResourceRepository supportingResourceRepository,
                           SupportingServiceRepository supportingServiceRepository, JsonPatcher jsonPatcher,
                           JsonMergePatcher jsonMergePatcher,
                           SpecificNotificationRepository specificNotificationRepository,
                           SpecificEventRepository specificEventRepository) {
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
    this.jsonPatcher = jsonPatcher;
    this.jsonMergePatcher = jsonMergePatcher;
    this.specificNotificationRepository = specificNotificationRepository;
    this.specificEventRepository = specificEventRepository;
  }

  //Method to return only the specified fields
  private MappingJacksonValue applyFieldFiltering(MappingJacksonValue mappingJacksonValue, MultiValueMap<String,
          String> params) {
    SimpleFilterProvider filters;
    if (params.containsKey("fields")) {
      filters = (new SimpleFilterProvider()).addFilter("service",
              SimpleBeanPropertyFilter.filterOutAllExcept((params.getFirst("fields")).split(",")));
      mappingJacksonValue.setFilters(filters);
      return mappingJacksonValue;
    } else {
      filters = (new SimpleFilterProvider()).addFilter("service", SimpleBeanPropertyFilter.serializeAll());
      mappingJacksonValue.setFilters(filters);
      return mappingJacksonValue;
    }
  }


  //TODO Handle queries in Swagger
  @ApiOperation(value="This operation list service entities.")
  @GetMapping("/service")
  @ResponseBody
  public MappingJacksonValue getServices(
          @ApiParam(name = "fields", value = "Fields to return", defaultValue = "")
          @RequestParam(value = "fields", required = false) String fields,
          @QuerydslPredicate(root = Service.class) Predicate predicate) {
    System.out.println("Predicate = " + predicate);
    System.out.println("Fields = " + fields);

    Iterable<Service> services = this.serviceRepository.findAll(predicate);
    MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(services);

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    if(fields != null) {
      params.add("fields", fields);
    }

    System.out.println("MultiValueMap = " + params);
    return applyFieldFiltering(mappingJacksonValue, params);
  }



  //TODO Return proper message back when resource isn't found (if(user==null))
  //Returns the service resource of the given id
  @ApiOperation(value="This operation retrives a service entity.")
  @GetMapping("/service/{id}")
  @ResponseBody
  public MappingJacksonValue getService(
          @PathVariable String id,
          @ApiParam(name = "fields", value = "fields", defaultValue = "")
          @RequestParam(value = "fields", required = false) String fields,
          @QuerydslPredicate(root = Service.class) Predicate predicate) {

    QService qService = QService.service;
    Predicate p = new BooleanBuilder(predicate);
    ((BooleanBuilder) p).and(qService.id.eq(id));
    Optional<Service> service = serviceRepository.findOne(p);
    MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(service);

    if(!service.isPresent()) {
      return new MappingJacksonValue("No service with parameters: " + p.toString());
    }

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    if (fields != null) {
      params.add("fields", fields);
    }

    return applyFieldFiltering(mappingJacksonValue, params);
  }

  //TODO currently working, but need to find a better way to return the created object
  //Creates a service in the database from the service JSON passed. Returns the created object.
  @ApiOperation(value="This operation creates a service entity")
  @PostMapping("/service")
  @Transactional
  @ResponseStatus(HttpStatus.CREATED)
  public MappingJacksonValue createService(@Valid @RequestBody Service service) {
    SimpleFilterProvider filters;
    filters = (new SimpleFilterProvider()).addFilter("service",
            SimpleBeanPropertyFilter.serializeAll());
    Service newService = serviceRepository.save(service);
    MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(newService);
    mappingJacksonValue.setFilters(filters);

//    SpecificNotification specificNotification = new SpecificNotification();
//    LocalDate localDate = LocalDate.now();
//    specificNotification.setEventTime(localDate.toString());
//    specificNotification.setEventType("ServiceCreationNotification");
//    SpecificEvent specificEvent = new SpecificEvent();
//    specificEvent.setService(newService);
//    specificNotification.setSpecificEvent(specificEvent);
//    specificNotificationRepository.save(specificNotification);

    return mappingJacksonValue;
  }


  //TODO Make it work in Swagger UI
  @ApiOperation(value="Partially updates a service entity")
  @Transactional
  @CrossOrigin
//  @PatchMapping(value = "/service/{id}")
  @RequestMapping(value = "/service/{id}", method = RequestMethod.PATCH)
  public MappingJacksonValue patchService(@RequestHeader("Content-Type") String contentType, @PathVariable String id, @RequestBody String updateResource) {
    QService qService = QService.service;
    Predicate p = new BooleanBuilder();
    ((BooleanBuilder) p).and(qService.id.eq(id));
    Optional<Service> optionalService = serviceRepository.findOne(p);
    if(!optionalService.isPresent()) {
      System.out.println("return null");
      return new MappingJacksonValue("No service with id = " + id);
    }
    Service resource = optionalService.get();
    if (contentType.equals("application/json")) {
      Optional<Service> patched = jsonMergePatcher.mergePatch(updateResource, resource);
      System.out.println(patched.get().getCategory());
      System.out.println(patched.get().getName());
      System.out.println(patched.get().getId());
      SimpleFilterProvider filters;
      filters = (new SimpleFilterProvider()).addFilter("service",
              SimpleBeanPropertyFilter.serializeAll());
      MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(serviceRepository.save(patched.get()));
      mappingJacksonValue.setFilters(filters);
//      SpecificNotification specificNotification = new SpecificNotification();
//      LocalDate localDate = LocalDate.now();
//      specificNotification.setEventTime(localDate.toString());
//      specificNotification.setEventType("ServiceAttributeValueChangeNotification");
//      SpecificEvent specificEvent = new SpecificEvent();
//      specificEvent.setService(patched.get());
//      specificNotification.setSpecificEvent(specificEvent);
//      specificNotificationRepository.save(specificNotification);
      return mappingJacksonValue;
    }/*
    else if (contentType.equals("application/json-patch+json")) {
      try {
        SimpleFilterProvider filters;
        filters = (new SimpleFilterProvider()).addFilter("service",
                SimpleBeanPropertyFilter.serializeAll());
        if (updateResource.startsWith("[")) {
          Optional<Service> patched = jsonPatcher.patch(updateResource, resource);
          MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(serviceRepository.save(patched.get()));
          mappingJacksonValue.setFilters(filters);
//          SpecificNotification specificNotification = new SpecificNotification();
//          LocalDate localDate = LocalDate.now();
//          specificNotification.setEventTime(localDate.toString());
//          specificNotification.setEventType("ServiceAttributeValueChangeNotification");
//          SpecificEvent specificEvent = new SpecificEvent();
//          specificEvent.setService(patched.get());
//          specificNotification.setSpecificEvent(specificEvent);
//          specificNotificationRepository.save(specificNotification);
          return mappingJacksonValue;
        }
        else {
          String updateResourceAsArray = "[" + updateResource + "]";
          Optional<Service> patched = jsonPatcher.patch(updateResourceAsArray, resource);
          MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(serviceRepository.save(patched.get()));
          mappingJacksonValue.setFilters(filters);
//          SpecificNotification specificNotification = new SpecificNotification();
//          LocalDate localDate = LocalDate.now();
//          specificNotification.setEventTime(localDate.toString());
//          specificNotification.setEventType("ServiceAttributeValueChangeNotification");
//          SpecificEvent specificEvent = new SpecificEvent();
//          specificEvent.setService(patched.get());
//          specificNotification.setSpecificEvent(specificEvent);
//          specificNotificationRepository.save(specificNotification);
          return mappingJacksonValue;
        }
      }
      catch (RuntimeException e) {
        System.out.println(e);
        if (JsonPatchException.class.isAssignableFrom(e.getCause().getClass())) {
          return new MappingJacksonValue("Not found");
        }
      }
      return new MappingJacksonValue("No content");
    }*/
    return new MappingJacksonValue("");
  }

  //TODO Proper exception handling for invalid id
  //Deletes the service at the given id
  @ApiOperation(value="This operation deletes a service entity.")
  @DeleteMapping("/service/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteService(@PathVariable String id) {
    QService qService = QService.service;
    Predicate p = new BooleanBuilder();
    ((BooleanBuilder) p).and(qService.id.eq(id));
    Optional<Service> optionalService = serviceRepository.findOne(p);
    if(!optionalService.isPresent()) {
      return;
    }
//    SpecificNotification specificNotification = new SpecificNotification();
//    LocalDate localDate = LocalDate.now();
//    specificNotification.setEventTime(localDate.toString());
//    specificNotification.setEventType("ServiceRemoveNotification");
//    SpecificEvent specificEvent = new SpecificEvent();
//    specificEvent.setService(optionalService.get());
//    specificNotification.setSpecificEvent(specificEvent);
//    specificNotificationRepository.save(specificNotification);
    serviceRepository.delete(optionalService.get());
  }

  //Deletes all services in the database
  @ApiOperation(value="This operation deletes all service entities.")
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
    String[] stateArray = {"active", "inactive", "terminated", "reserved", "designed", "feasabilityChecked"};
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
      service.setServiceEnabled(booleanArray[random.nextInt(2)]);
      service.setStateful(booleanArray[random.nextInt(2)]);
      service.setStartMode(startModeArray[random.nextInt(startModeArray.length)]);
      service.setState(stateArray[random.nextInt(stateArray.length)]);
      service.setType(typeArray[random.nextInt(typeArray.length)]);
      service.setServiceSpecification(serviceSpecificationArray[j]);
      serviceRepository.save(service);
    }
  }

}
