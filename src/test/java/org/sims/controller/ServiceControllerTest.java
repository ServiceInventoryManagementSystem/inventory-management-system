//package org.sims.controller;
//
//import com.querydsl.core.types.Predicate;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.sims.model.QService;
//import org.sims.model.Service;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.rest.webmvc.ResourceNotFoundException;
//import org.springframework.http.converter.json.MappingJacksonValue;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import static org.junit.Assert.*;
//
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class ServiceControllerTest {
//
//  @Autowired
//  private ServiceController serviceController;
//
//  @Before
//  public void setUp() {
//
//  }
//
//  @After
//  public void tearDown() {
//  }
//
//  @Test
//  public void getServiceTest() {
//    QService service = QService.service;
//    Predicate predicate = service.isNotNull();
//    String args = "";
//    MappingJacksonValue mjv = serviceController.getService("1", args, predicate);
//    if (mjv == null) {
//      fail();
//    }
//    Object o = mjv.getValue();
//    Optional<Service> ser = o instanceof Optional ? ((Optional) o) : Optional.empty();
//    if(!ser.isPresent()) {
//      fail();
//    }
//    Service s = ser.get();
//    assertEquals("Email", s.getName());
//  }
//
//  @Test
//  public void getServicesTest() {
//    QService service = QService.service;
//    Predicate predicate = service.isNotNull();
//    String args = "";
//    MappingJacksonValue mjv = serviceController.getServices(args, predicate);
//    if (mjv == null) {
//      fail();
//    }
//    Object o = mjv.getValue();
//    List<Service> serv = o instanceof List ? ((List) o) : null;
//    if(serv == null) {
//      fail();
//    }
//    assertEquals("Email", serv.get(0).getName());
//    assertEquals("Search", serv.get(1).getName());
//  }
//
//  @Test
//  @DirtiesContext
//  public void createServiceTest() {
//    Service service = new Service();
//    service.setName("createdServiceName");
//    service.setCategory("createdServiceCategory");
//    serviceController.createService(service);
//
//    QService qService = QService.service;
//    Predicate predicate = qService.isNotNull();
//    String args = "";
//
//    MappingJacksonValue mappingJacksonValue = serviceController.getService("3", args, predicate);
//    Object object = mappingJacksonValue.getValue();
//    Optional<Service> optionalService = object instanceof Optional ? ((Optional) object) : Optional.empty();
//    if(!optionalService.isPresent()) {
//      fail();
//    }
//    Service createdService = optionalService.get();
//    assertEquals("createdServiceName", createdService.getName());
//    assertEquals("createdServiceCategory", createdService.getCategory());
//  }
//
//  @Test
//  @DirtiesContext
//  public void patchServiceTest() {
//    QService qService = QService.service;
//    Predicate predicate = qService.isNotNull();
//
//    String args = "";
//    MappingJacksonValue preMappingJacksonValue = serviceController.getService("1", args, predicate);
//    Object preObject = preMappingJacksonValue.getValue();
//    Optional<Service> preOptionalService = preObject instanceof Optional ? ((Optional) preObject) : Optional.empty();
//
//    if(!preOptionalService.isPresent()) {
//      fail();
//    }
//    Service preService = preOptionalService.get();
//    assertEquals("Email", preService.getName());
//
//    serviceController.patchService("application/merge-patch+json", "1", "{\"name\": \"postPatchName\"}");
//    MappingJacksonValue postMappingJacksonValue = serviceController.getService("1", args, predicate);
//    Object postObject = postMappingJacksonValue.getValue();
//    Optional<Service> postOptionalService = postObject instanceof Optional ? ((Optional) postObject) : Optional.empty();
//
//    if (!postOptionalService.isPresent()) {
//      fail();
//    }
//
//    Service postService = postOptionalService.get();
//    assertEquals("postPatchName", postService.getName());
//  }
//
//  @Test(expected = ResourceNotFoundException.class)
//  @DirtiesContext
//  public void deleteServiceTest() {
//    Service service = new Service();
//    service.setName("createdServiceName");
//    service.setCategory("createdServiceCategory");
//    serviceController.createService(service);
//
//    QService qService = QService.service;
//    Predicate predicate = qService.isNotNull();
//
//    String args = "";
//    MappingJacksonValue mappingJacksonValue = serviceController.getService("3", args, predicate);
//    Object object = mappingJacksonValue.getValue();
//    Optional<Service> optionalService = object instanceof Optional ? ((Optional) object) : Optional.empty();
//
//    if(!optionalService.isPresent()) {
//      fail();
//    }
//    Service createdService = optionalService.get();
//
//    assertEquals("createdServiceName", createdService.getName());
//    assertEquals("createdServiceCategory", createdService.getCategory());
//
//    serviceController.deleteService("3");
//
//    Object emptyServiceObject = serviceController.getService("3", args, predicate).getValue();
//    Optional<Service> optionalEmptyService = emptyServiceObject instanceof Optional ? ((Optional) emptyServiceObject) : Optional.empty();
//    assertTrue(!optionalEmptyService.isPresent());
//  }
//
//  @Test
//  @DirtiesContext
//  public void deleteAllServicesTest() {
//    serviceController.deleteServices();
//    QService service = QService.service;
//    Predicate predicate = service.isNotNull();
//    String args = "";
//    MappingJacksonValue mjv = serviceController.getServices(args, predicate);
//    if (mjv == null) {
//      fail();
//    }
//    Object o = mjv.getValue();
//    List<Service> serv = o instanceof List ? ((List) o) : null;
//    if(serv == null) {
//      fail();
//    }
//    assert serv.isEmpty();
//  }
//
//  @Test
//  @DirtiesContext
//  public void seedDatabaseTest() {
//    QService service = QService.service;
//    Predicate predicate = service.isNotNull();
//    String args = "";
//    MappingJacksonValue mjv = serviceController.getServices(args, predicate);
//    Object o = mjv.getValue();
//    List<Service> serv = o instanceof List ? ((List) o) : null;
//
//    int preSeedCount = serv.size();
//
//    serviceController.seed(50);
//
//    mjv = serviceController.getServices(args, predicate);
//
//    o = mjv.getValue();
//    serv = o instanceof List ? ((List) o) : null;
//
//    assert serv.size() == preSeedCount + 50;
//
//
//  }
//
//
//
//
//}
