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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.MediaTypeNotSupportedStatusException;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.*;


@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class ServiceController implements Serializable {

  private final ServiceRepository serviceRepository;
  private final RelatedPartyRepository relatedPartyRepository;
  private final ServiceSpecificationRepository serviceSpecificationRepository;
  private final HubRepository hubRepository;
  private JsonPatcher jsonPatcher;
  private JsonMergePatcher jsonMergePatcher;
  private SpecificNotificationRepository specificNotificationRepository;
  private final NoteRepository noteRepository;
  private final PlaceRepository placeRepository;
  private final ServiceCharacteristicRepository serviceCharacteristicRepository;

  // Repositories are commented out because they're not used. Uncomment to implement in seed method.
  /*
  private final ServiceOrderRepository serviceOrderRepository;
  private final ServiceRefRepository serviceRefRepository;
  private final ServiceRelationshipRepository serviceRelationshipRepository;
  private final SupportingResourceRepository supportingResourceRepository;
  private final SupportingServiceRepository supportingServiceRepository;
  */

  @Autowired
  public ServiceController(ServiceRepository serviceRepository,
                           RelatedPartyRepository relatedPartyRepository,
                           ServiceSpecificationRepository serviceSpecificationRepository,
                           JsonPatcher jsonPatcher,
                           JsonMergePatcher jsonMergePatcher,
                           SpecificNotificationRepository specificNotificationRepository,
                           HubRepository hubRepository,
                           NoteRepository noteRepository,
                           PlaceRepository placeRepository,
                           ServiceCharacteristicRepository serviceCharacteristicRepository

                           /*
                           ServiceOrderRepository serviceOrderRepository,
                           ServiceRefRepository serviceRefRepository,
                           ServiceRelationshipRepository serviceRelationshipRepository,
                           SupportingResourceRepository supportingResourceRepository,
                           SupportingServiceRepository supportingServiceRepository*/) {
    this.serviceRepository = serviceRepository;
    this.relatedPartyRepository = relatedPartyRepository;
    this.serviceSpecificationRepository = serviceSpecificationRepository;
    this.jsonPatcher = jsonPatcher;
    this.jsonMergePatcher = jsonMergePatcher;
    this.specificNotificationRepository = specificNotificationRepository;
    this.hubRepository = hubRepository;
    this.noteRepository = noteRepository;
    this.placeRepository = placeRepository;
    this.serviceCharacteristicRepository = serviceCharacteristicRepository;
    /*
    this.serviceOrderRepository = serviceOrderRepository;
    this.serviceRefRepository = serviceRefRepository;
    this.serviceRelationshipRepository = serviceRelationshipRepository;
    this.supportingResourceRepository = supportingResourceRepository;
    this.supportingServiceRepository = supportingServiceRepository;
    */
  }

  // Method to return only the specified fields
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


  // Returns all services matching a query. Pagination has been added for better efficiency on the front end.
  // The following request returns the 10 first services where category = CFS. Only the id and category fields of each service is returned.
  // localhost:3000/api/service?fields=id,category&category=CFS&page=0&size=10
  @ApiOperation(value = "Returns all service entities. ?fields= determines the fields that are returned. " +
          "Querying is currently not supported in Swagger UI")
  @GetMapping("/service")
  @ResponseBody
  public MappingJacksonValue getServices(
          @ApiParam(name = "fields", value = "Fields to return", defaultValue = "")
          @RequestParam(value = "fields", required = false) String fields,
          @QuerydslPredicate(root = Service.class) Predicate predicate,
          @PageableDefault(size = 1000000) Pageable pageable) {
    Iterable<Service> services = serviceRepository.findAll(predicate, pageable);
    List<Service> servicePage = ((Page<Service>) services).getContent();

    MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(servicePage);
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    if(fields != null) {
      params.add("fields", fields);
    }
    return applyFieldFiltering(mappingJacksonValue, params);
  }



  // Returns the service resource of the given id. Querying and specifying fields to be returned is also possible
  // The following request returns the service with id of 1 and category = CFS. Only the id and category fields are returned.
  // localhost:3000/api/service/1?fields=category,id&category=CFS
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
    if(!service.isPresent()) {
      throw new ResourceNotFoundException();
    }
    MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(service);
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    if (fields != null) {
      params.add("fields", fields);
    }
    return applyFieldFiltering(mappingJacksonValue, params);
  }

  // Creates a service in the database. Returns the created object. Sends a "ServiceCreationNotification" to all listeners in the hub.
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


  // Partially updates a service in the database. Sends a "ServiceAttributeValueChangeNotification" or "ServiceStateChangeNotification" to all listeners in the hub.
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
    String preState = resource.getState();
    if (contentType.equals("application/merge-patch+json")) {
      Optional<Service> patched = jsonMergePatcher.mergePatch(updateResource, resource);
      SimpleFilterProvider filters = new SimpleFilterProvider();
      filters.setFailOnUnknownId(false);
      Service patchedService = serviceRepository.save(patched.get());
      if (stringCompare(preState, patchedService.getState())) {
        sendNotification("ServiceAttributeValueChangeNotification", patchedService, filters);
      }
      else {
        sendNotification("ServiceStateChangeNotification", patchedService, filters);
      }
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

          if (stringCompare(preState, patchedService.getState())) {
            sendNotification("ServiceAttributeValueChangeNotification", patchedService, filters);
          }
          else {
            sendNotification("ServiceStateChangeNotification", patchedService, filters);
          }

          MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(patchedService);
          mappingJacksonValue.setFilters(filters);
          return mappingJacksonValue;
        }
        else {
          String updateResourceAsArray = "[" + updateResource + "]";
          Optional<Service> patched = jsonPatcher.patch(updateResourceAsArray, resource);
          Service patchedService = serviceRepository.save(patched.get());
          if (stringCompare(preState, patchedService.getState())) {
            sendNotification("ServiceAttributeValueChangeNotification", patchedService, filters);
          }
          else {
            sendNotification("ServiceStateChangeNotification", patchedService, filters);
          }
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

  // Method used to check if the state is the same before and after a PATCH request
  private Boolean stringCompare(String str1, String str2) {
    return (str1 == null ? str2 == null : str1.equals(str2));
  }


  // Deletes the service at the given id. Sends a "ServiceRemoveNotification" to all listeners in the hub.
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


  // Seeds the database with some randomly generated services.
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
    String[] hrefArray = {"http://server:port/serviceInventory/service/id"};
    Boolean[] booleanArray = {true, false};
    String[] startModeArray = {"0", "1", "2", "3", "4", "5", "6"};
    String[] stateArray = {"active", "inactive", "terminated", "reserved", "designed", "feasibilityChecked"};
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
      service.setDescription("Description of service");
      service.setHref(hrefArray[random.nextInt(hrefArray.length)]);
      service.setHasStarted(booleanArray[random.nextInt(2)]);
      service.setIsServiceEnabled(booleanArray[random.nextInt(2)]);
      service.setIsStateful(booleanArray[random.nextInt(2)]);
      service.setStartMode(startModeArray[random.nextInt(startModeArray.length)]);
      service.setState(stateArray[random.nextInt(stateArray.length)]);
      service.setType(typeArray[random.nextInt(typeArray.length)]);
      service.setServiceSpecification(serviceSpecificationArray[j]);

      RelatedParty relatedParty = new RelatedParty();
      relatedParty.setHref("http://serverlocation:port/partyManagement/organisation/" + Integer.toString(j));
      relatedParty.setJsonId(Integer.toString(j));
      relatedParty.setRole("partner");

      Set<RelatedParty> relatedParties = new HashSet<>();
      relatedParties.add(relatedParty);
      service.setRelatedParties(relatedParties);

      Set<Service> services = new HashSet<>();
      services.add(service);
      relatedParty.setServices(services);
      relatedPartyRepository.save(relatedParty);

      Place place = new Place();
      place.setHref("http://place.com");
      place.setRole("role");
      place.setService(service);
      placeRepository.save(place);
      Set<Place> places = new HashSet<>();
      places.add(place);
      service.setPlaces(places);

      Note note = new Note();
      note.setAuthor("Author");
      note.setText("Note text");
      note.setService(service);
      noteRepository.save(note);
      Set<Note> notes = new HashSet<>();
      notes.add(note);
      service.setNotes(notes);

      ServiceCharacteristic serviceCharacteristic = new ServiceCharacteristic();
      serviceCharacteristic.setService(service);
      serviceCharacteristic.setName("Speed");
      serviceCharacteristic.setValue("16M");
      serviceCharacteristicRepository.save(serviceCharacteristic);
      List<ServiceCharacteristic> serviceCharacteristics = new ArrayList<>();
      serviceCharacteristics.add(serviceCharacteristic);
      service.setServiceCharacteristics(serviceCharacteristics);


      serviceRepository.save(service);
    }
  }

}
