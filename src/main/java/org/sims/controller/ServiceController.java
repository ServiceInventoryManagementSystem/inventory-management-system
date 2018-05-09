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
import org.sims.model.QService;
import org.sims.model.Service;
import org.sims.model.ServiceSpecification;
import org.sims.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
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


  @Autowired
  public ServiceController(ServiceRepository serviceRepository, NoteRepository noteRepository,
                           PlaceRepository placeRepository, RelatedPartyRepository relatedPartyRepository,
                           ServiceCharacteristicRepository serviceCharacteristicRepository,
                           ServiceOrderRepository serviceOrderRepository, ServiceRefRepository serviceRefRepository,
                           ServiceRelationshipRepository serviceRelationshipRepository,
                           ServiceSpecificationRepository serviceSpecificationRepository,
                           SupportingResourceRepository supportingResourceRepository,
                           SupportingServiceRepository supportingServiceRepository, JsonPatcher jsonPatcher,
                           JsonMergePatcher jsonMergePatcher) {
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
    MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(serviceRepository.save(service));
    mappingJacksonValue.setFilters(filters);
    return mappingJacksonValue;
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
    String[] stateArray = {"active", "inactive", "terminated"};
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

  @Transactional
  @RequestMapping(value = "/service/{id}", method = RequestMethod.PATCH)
  public MappingJacksonValue patchService(@RequestHeader("Content-Type") String contentType, @PathVariable String id, @RequestBody String updateResource) {
    System.out.println(contentType);
    System.out.println(contentType);
    System.out.println(contentType);
    System.out.println(contentType);


    QService qService = QService.service;
    Predicate p = new BooleanBuilder();
    ((BooleanBuilder) p).and(qService.id.eq(id));
    Optional<Service> optionalService = serviceRepository.findOne(p);
    if(!optionalService.isPresent()) {
      System.out.println("return null");
      return null;
    }
    Service resource = optionalService.get();
    if (contentType.equals("application/merge-patch+json")) {
      Optional<Service> patched = jsonMergePatcher.mergePatch(updateResource, resource);
      System.out.println(patched.get().getCategory());
      System.out.println(patched.get().getName());
      System.out.println(patched.get().getId());
      SimpleFilterProvider filters;
      filters = (new SimpleFilterProvider()).addFilter("service",
              SimpleBeanPropertyFilter.serializeAll());
      MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(serviceRepository.save(patched.get()));
      mappingJacksonValue.setFilters(filters);
      return mappingJacksonValue;
    }
    else if (contentType.equals("application/json-patch+json")) {
      try {
        StringBuilder _sb = new StringBuilder(updateResource);
        _sb.insert(0, "[");
        _sb.append("]");
        System.out.println(updateResource);
        System.out.println(_sb);
        Optional<Service> patched = jsonPatcher.patch(_sb.toString(), resource);
        SimpleFilterProvider filters;
        filters = (new SimpleFilterProvider()).addFilter("service",
                SimpleBeanPropertyFilter.serializeAll());
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(serviceRepository.save(patched.get()));
        mappingJacksonValue.setFilters(filters);
        return mappingJacksonValue;      }
      catch (RuntimeException e) {
        System.out.println(e);
        if (JsonPatchException.class.isAssignableFrom(e.getCause().getClass())) {
          return new MappingJacksonValue("Not found");
        }
      }
      return new MappingJacksonValue("No content");
    }
    return new MappingJacksonValue("");
  }

  /*
  	@RequestMapping(
			value = "/v3/persons/{id}",
			method = RequestMethod.PATCH,
			consumes = RestMediaType.APPLICATION_PATCH_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PersonResource> updatePartial(@PathVariable Integer id, @RequestBody String updateResource) {
		PersonResource resource = new ResourceBuilder().build();

		try {
			Optional<PersonResource> patched = jsonPatcher.patch(updateResource, resource);
			return new ResponseEntity<>(patched.get(), HttpStatus.OK);
		}
		catch (RuntimeException e) {
			if (JsonPatchException.class.isAssignableFrom(e.getCause().getClass())) {
				return new ResponseEntity<>(resource, HttpStatus.NOT_FOUND);
			}
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
   */

}
