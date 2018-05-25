package org.sims.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.github.fge.jsonpatch.JsonPatchException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.sims.controller.common.JsonMergePatcher;
import org.sims.controller.common.JsonPatcher;
import org.sims.model.*;
import org.sims.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.AbstractPageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.MediaTypeNotSupportedStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.time.LocalDate;
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
  private final ObjectMapper objectMapper;
  private final HubRepository hubRepository;

  private JsonPatcher jsonPatcher;
  private JsonMergePatcher jsonMergePatcher;

  private SpecificNotificationRepository specificNotificationRepository;
//  private SpecificEventRepository specificEventRepository;


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
                           SpecificNotificationRepository specificNotificationRepository/*,
                           SpecificEventRepository specificEventRepository*/,
                           ObjectMapper objectMapper,
                           HubRepository hubRepository) {
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
    this.specificNotificationRepository = specificNotificationRepository;/*
    this.specificEventRepository = specificEventRepository;*/
    this.objectMapper = objectMapper;
    this.hubRepository = hubRepository;
  }

  //Method to return only the specified fields
  private MappingJacksonValue applyFieldFiltering(MappingJacksonValue mappingJacksonValue, MultiValueMap<String,
          String> params) {
    SimpleFilterProvider filters = new SimpleFilterProvider();
    if (params.containsKey("fields")) {
      filters = (new SimpleFilterProvider()).addFilter("service",
              SimpleBeanPropertyFilter.filterOutAllExcept((params.getFirst("fields")).split(",")));
      mappingJacksonValue.setFilters(filters);
      return mappingJacksonValue;
    } else {
      filters.setFailOnUnknownId(false);
      mappingJacksonValue.setFilters(filters);
      return mappingJacksonValue;
    }
  }




  @ApiOperation(value = "Returns all service entities. ?fields= determines the fields that are returned. Querying is currently not supported in Swagger UI")
  @GetMapping("/service")
  @ResponseBody
  public MappingJacksonValue getServices(
          @ApiParam(name = "fields", value = "Fields to return", defaultValue = "")
          @RequestParam(value = "fields", required = false) String fields,
//          @RequestParam(required = false)
          @QuerydslPredicate(root = Service.class) Predicate predicate,
          @PageableDefault(size = 10000) Pageable pageable) {

    System.out.println(predicate);
    System.out.println(predicate);
    System.out.println(predicate);
    System.out.println(predicate);
    System.out.println(predicate);

    Iterable<Service> services = serviceRepository.findAll(predicate, pageable);
    List<Service> servicePage = ((Page<Service>) services).getContent();

    MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(servicePage);

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    if(fields != null) {
      params.add("fields", fields);
    }

    return applyFieldFiltering(mappingJacksonValue, params);
  }



  //Returns the service resource of the given id
  @ApiOperation(value = "Returns the service entity with the given id. ?fields= determines the fields that are returned. Querying is currently not supported in Swagger UI")
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
      throw new ResourceNotFoundException();
    }

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    if (fields != null) {
      params.add("fields", fields);
    }

    return applyFieldFiltering(mappingJacksonValue, params);
  }

  //Creates a service in the database from the service JSON passed. Returns the created object.
  @ApiOperation(value = "Creates a service entity")
  @PostMapping("/service")
  @Transactional
  @ResponseStatus(HttpStatus.CREATED)
  public MappingJacksonValue createService(@Valid @RequestBody Service service) {
    SimpleFilterProvider filters = new SimpleFilterProvider();
    filters.setFailOnUnknownId(false);
    Service newService = serviceRepository.save(service);
    MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(newService);
    mappingJacksonValue.setFilters(filters);

    sendNotification("ServiceCreationNotification", newService, filters);

    return mappingJacksonValue;
  }

  // Sends a notification to all the callback addresses in the hub repository
  private void sendNotification(String eventType, Service service, SimpleFilterProvider filters) {
    Event event = new Event();
    event.setEventType(eventType);

    SpecificNotification specificNotification = new SpecificNotification();
    specificNotification.setEventType(eventType);
    SpecificEvent specificEvent = new SpecificEvent();
    specificEvent.setService(service);
    specificNotification.setSpecificEvent(specificEvent);
    SpecificNotification savedSpecificNotification = specificNotificationRepository.save(specificNotification);
    event.setSpecificNotification(savedSpecificNotification);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    MappingJacksonValue mjv = new MappingJacksonValue(event);
    mjv.setFilters(filters);

    HttpEntity<MappingJacksonValue> request = new HttpEntity<>(mjv, headers);
    RestTemplate restTemplate = new RestTemplate();

    Iterable<Hub> hubIterable = hubRepository.findAll();
    for (Hub hub: hubIterable) {
      try {
      restTemplate.postForObject(hub.getCallback(), request, String.class);
      }
      catch (Exception e){
        System.err.println(e);
      }
    }
  }


  @ApiOperation(value = "Partially updates a service resource with the given id. Currently only RFC 7386 is supported in Swagger UI",
          consumes = "application/merge-patch+json")
  @Transactional
  @PatchMapping(value = "/service/{id}")
  public MappingJacksonValue patchService(@RequestHeader("Content-Type") String contentType, @PathVariable String id, @RequestBody String updateResource) {
    QService qService = QService.service;
    Predicate p = new BooleanBuilder();
    ((BooleanBuilder) p).and(qService.id.eq(id));
    Optional<Service> optionalService = serviceRepository.findOne(p);
    if(!optionalService.isPresent()) {
      throw new ResourceNotFoundException();
    }
    Service resource = optionalService.get();
    if (contentType.equals("application/merge-patch+json")) {
      Optional<Service> patched = jsonMergePatcher.mergePatch(updateResource, resource);
      SimpleFilterProvider filters = new SimpleFilterProvider();
      filters.setFailOnUnknownId(false);
      Service patchedService = serviceRepository.save(patched.get());
      sendNotification("ServiceAttributeValueChangeNotification", patchedService, filters);

      MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(patchedService);
      mappingJacksonValue.setFilters(filters);
      return mappingJacksonValue;
    }
    else if (contentType.equals("application/json-patch+json")) {
      try {
        SimpleFilterProvider filters = new SimpleFilterProvider();
        filters.setFailOnUnknownId(false);
        if (updateResource.startsWith("[")) {
          Optional<Service> patched = jsonPatcher.patch(updateResource, resource);
          Service patchedService = serviceRepository.save(patched.get());
          sendNotification("ServiceAttributeValueChangeNotification", patchedService, filters);

          MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(patchedService);
          mappingJacksonValue.setFilters(filters);
          return mappingJacksonValue;
        }
        else {
          String updateResourceAsArray = "[" + updateResource + "]";
          Optional<Service> patched = jsonPatcher.patch(updateResourceAsArray, resource);
          Service patchedService = serviceRepository.save(patched.get());
          sendNotification("ServiceAttributeValueChangeNotification", patchedService, filters);

          MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(patchedService);
          mappingJacksonValue.setFilters(filters);
          return mappingJacksonValue;
        }
      }
      catch (RuntimeException e) {
        System.out.println(e);
        if (JsonPatchException.class.isAssignableFrom(e.getCause().getClass())) {
          return new MappingJacksonValue("Not found");
        }
      }
    }
    throw new MediaTypeNotSupportedStatusException("Content-Type not supported");
  }

  //Deletes the service at the given id
  @ApiOperation(value = "Deletes the service with the given id from the database.")
  @DeleteMapping("/service/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteService(@PathVariable String id) {
    QService qService = QService.service;
    Predicate p = new BooleanBuilder();
    ((BooleanBuilder) p).and(qService.id.eq(id));
    Optional<Service> optionalService = serviceRepository.findOne(p);
    if(!optionalService.isPresent()) {
      throw new ResourceNotFoundException();
    }
    Service service = optionalService.get();

    SimpleFilterProvider filters = new SimpleFilterProvider();
    filters.setFailOnUnknownId(false);
    sendNotification("ServiceRemoveNotification", optionalService.get(), filters);



    serviceRepository.delete(service);


  }

  //Deletes all services in the database
  @ApiOperation(value = "Deletes all service entities in the database.")
  @DeleteMapping("/service")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteServices() {
    serviceRepository.deleteAll();
  }


  @ApiOperation(value= "Seeds the database with some randomly generated services. Count is the number of services.")
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
