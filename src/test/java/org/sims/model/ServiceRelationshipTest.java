package org.sims.model;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class ServiceRelationshipTest {
  // This test was not performed with OpenPojo. When returning a service, the serviceRelationship object
  // should contain a reference to another service. This would normally be solved by using @JsonProperty(""), but
  // this caused some issues. Spring returns the properties based on the getter's name, so the easiest fix was to rename
  // the serviceRef getter to getService().
  @Test
  public void validateSettersAndGetters() {
    Service service = new Service();
    service.setName("name");
    ServiceRelationship serviceRelationship = new ServiceRelationship();
    serviceRelationship.setId(1L);
    serviceRelationship.setOwningService(service);
    serviceRelationship.setType("type");
    ServiceRef serviceRef = new ServiceRef();
    serviceRef.setServiceRelationship(serviceRelationship);
    serviceRelationship.setServiceRef(serviceRef);
    Set<ServiceRelationship> serviceRelationships = new HashSet<>();
    serviceRelationships.add(serviceRelationship);
    service.setServiceRelationships(serviceRelationships);

    assert 1L == serviceRelationship.getId();
    assert service == serviceRelationship.getOwningService();
    assert "type".equals(serviceRelationship.getType());
    assert serviceRef == serviceRelationship.getService();
  }
}
