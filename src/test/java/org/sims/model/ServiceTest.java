package org.sims.model;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;



//@RunWith(SpringRunner.class)
//@SpringBootTest
public class ServiceTest {
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
  }
}
