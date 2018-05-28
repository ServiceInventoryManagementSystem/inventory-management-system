package org.sims.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sims.model.Hub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HubControllerTest {

  @Autowired
  private HubController hubController;

  @Before
  public void setUp() {

  }

  @After
  public void tearDown() {
  }

  @Test
  @DirtiesContext
  public void registerListenerTest() {
    Hub hub = new Hub();
    hub.setCallback("http://in.listener.com");
    hubController.registerListener(hub);

    List<Hub> hubs = hubController.getHubs();
    assertEquals("http://in.listener.com", hubs.get(0).getCallback());
  }

  @Test
  @DirtiesContext
  public void unregisterListenerTest() {
    Hub hub = new Hub();
    hub.setCallback("http://in.listener.com");
    hubController.registerListener(hub);
    hubController.unregisterListener("1");
    List<Hub> hubs = hubController.getHubs();
    assert hubs.size() == 0;
  }

}
