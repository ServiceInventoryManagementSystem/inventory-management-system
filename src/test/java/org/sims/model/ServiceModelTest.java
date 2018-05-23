package org.sims.model;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.OffsetDateTime;

import static org.junit.Assert.*;



//@RunWith(SpringRunner.class)
//@SpringBootTest
public class ServiceModelTest {
  @Before
  public void setUp() {

  }

  @After
  public void tearDown() {
  }

  @Test
  public void primaryAttributeTest() {
    Service service = new Service();

    service.setName("asd");
    String name = service.getName();
    assertEquals("asd", service.getName());
    assertEquals("asd", name);

    service.setCategory("CFS");
    String category = service.getCategory();
    assertEquals("CFS", service.getCategory());
    assertEquals("CFS", category);

    service.setDescription("Broadband");
    String description = service.getDescription();
    assertEquals("Broadband", service.getDescription());
    assertEquals("Broadband", description);

    OffsetDateTime odt = OffsetDateTime.parse("2017-11-15T08:22:12+01:00");
    service.setEndDate(odt);
    assertEquals(odt, service.getEndDate());

    service.setServiceEnabled(true);
    assertEquals(true, service.getServiceEnabled());

    service.setStateful(true);
    assertEquals(true, service.getStateful());

    service.setOrderDate(odt);
    assertEquals(odt, service.getOrderDate());

    service.setStartDate(odt);
    assertEquals(odt, service.getStartDate());

    service.setStartMode("0");
    assertEquals("0", service.getStartMode());

    service.setState("terminated");
    assertEquals("terminated", service.getState());

    service.setType("type");
    assertEquals("type", service.getType());
  }

  @Test
  public void ServiceSpecificationTest() {
    ServiceSpecification serviceSpecification = new ServiceSpecification();
    Service service = new Service();
    service.setName("name");
    serviceSpecification.setName("name");
    serviceSpecification.setHref("href");
    serviceSpecification.setJsonId("1");
    serviceSpecification.setService(service);
    serviceSpecification.setVersion("version");
    service.setServiceSpecification(serviceSpecification);
    assert service.getServiceSpecification() == serviceSpecification;
    assertEquals("name", serviceSpecification.getName());
    assertEquals("href", serviceSpecification.getHref());
    assertEquals("1", serviceSpecification.getJsonId());
    assertEquals("version", serviceSpecification.getVersion());
  }


}
