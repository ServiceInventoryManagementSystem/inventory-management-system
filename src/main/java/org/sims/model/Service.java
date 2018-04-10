package org.sims.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;

@Entity
@JsonFilter("org.sims.model.Service")
public class Service {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String category;
  private String description;
  private String endDate;
  private Boolean hasStarted;
  private String href;
//  private String id;
  private Boolean isServiceEnabled;
  private Boolean isStateful;
  private String name;
  private String orderDate;
  private String startDate;
  private String startMode;
  private String state;
  private String type;


  private String uuid;
  public String getUuid() {
    return uuid;
  }
  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  //---------------------------------------Relations------------------------------------------------------------------
  //---------------------------------------OneToOne-------------------------------------------------------------------

  @OneToOne(cascade = CascadeType.ALL)
  private ServiceSpecification serviceSpecification;


  //--------------------------------------OneToMany-------------------------------------------------------------------
  @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
  private Set<Note> notes = new HashSet<>();

  @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
  private Set<Place> places = new HashSet<>();

  @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
  private Set<ServiceCharacteristic> serviceCharacteristics = new HashSet<>();

  @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
  private Set<ServiceRelationship> serviceRelationships = new HashSet<>();



  //----------------------------------------ManyToOne-----------------------------------------------------------------

  @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST})
  private ServiceOrder serviceOrder;


  //----------------------------------------ManyToMany----------------------------------------------------------------

  @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.PERSIST})
  @JoinTable(name = "SERVICE_RELATED_PARTY",
          joinColumns = @JoinColumn(name = "SERVICE_ID"),
          inverseJoinColumns = @JoinColumn(name = "RELATED_PARTY_ID"))
  private Set<RelatedParty> relatedParties = new HashSet<>();

  @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.PERSIST})
  @JoinTable(name = "SERVICE_SUPPORTING_RESOURCE",
          joinColumns = @JoinColumn(name = "SERVICE_ID"),
          inverseJoinColumns = @JoinColumn(name = "SUPPORTING_RESOURCE_ID"))
  private List<SupportingResource> supportingResources = new ArrayList<>();

  @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.PERSIST})
  @JoinTable(name = "SERVICE_SUPPORTING_SERVICE",
          joinColumns = @JoinColumn(name = "SERVICE_ID"),
          inverseJoinColumns = @JoinColumn(name = "SUPPORTING_SERVICE_ID"))
  private List<SupportingService> supportingServices = new ArrayList<>();


  //-----------------------------------------Constructor--------------------------------------------------------------

  public Service() {
  }


  //-----------------------------Getters and Setters for non-relations------------------------------------------------

  public Long getId() {
    return this.id;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public Boolean getHasStarted() {
    return hasStarted;
  }

  public void setHasStarted(Boolean hasStarted) {
    this.hasStarted = hasStarted;
  }

  public String getHref() {
    return href;
  }

  public void setHref(String href) {
    this.href = href;
  }

  public Boolean getIsServiceEnabled() {
    return isServiceEnabled;
  }

  public void setIsServiceEnabled(Boolean serviceEnabled) {
    isServiceEnabled = serviceEnabled;
  }

  public Boolean getIsStateful() {
    return isStateful;
  }

  public void setIsStateful(Boolean stateful) {
    isStateful = stateful;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(String orderDate) {
    this.orderDate = orderDate;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getStartMode() {
    return startMode;
  }

  public void setStartMode(String startMode) {
    this.startMode = startMode;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }


  //--------------------------------Getters and Setters for relations-------------------------------------------------

  public ServiceSpecification getServiceSpecification() {
    return serviceSpecification;
  }

  public void setServiceSpecification(ServiceSpecification serviceSpecification) {
    this.serviceSpecification = serviceSpecification;
  }

//  //TODO update currently replaces the existing servicespecification with the given parameters, even if they're not present
//  //Creates or updates service specification depending on the value of op
//  public void customSetServiceSpecification(LinkedHashMap lhm, String op) {
//    if (op.equals("update")) {
//      System.out.println("\nUPDATING EXISTING SERVICESPECIFICATION \n");
//      //TODO Add a check to see which attributes are passed before applying.
//      this.serviceSpecification.setVersion((String) lhm.get("version"));
//      this.serviceSpecification.setName((String) lhm.get("name"));
//      this.serviceSpecification.setId((String) lhm.get("id"));
//      this.serviceSpecification.setHref((String) lhm.get("href"));
//    } else if (op.equals("replace")) {
//      System.out.println("\nCREATING NEW SERVICESPECIFICATION \n");
//      ServiceSpecification serviceSpecification = new ServiceSpecification();
//      serviceSpecification.setVersion((String) lhm.get("version"));
//      serviceSpecification.setName((String) lhm.get("name"));
//      serviceSpecification.setId((String) lhm.get("id"));
//      serviceSpecification.setHref((String) lhm.get("href"));
//      this.setServiceSpecification(serviceSpecification);
//    }
//  }

    /*
    //TODO be able to update id of object, currently not working, not part of the API
    //Updates / removes the relationship between a service and a ServiceSpecification to the given id
    public void customSetServiceSpecification(Integer id, String op) {

        System.out.println("Entered Integer changer");
        String lewl = id.toString();
        System.out.println("Set lewl to " + lewl);
        Long lul = new Long(lewl);
        System.out.println("Set lul to " + lul);

        EntityHelper eh = new EntityHelper();
        System.out.println("Created EntityHelper");

        ServiceSpecification ser = eh.findById(lul);
        System.out.println("Found ServiceSpecification by id, = " + ser);
        this.setServiceSpecification(ser);
        System.out.println("\nChange ID\nChange ID\nChange ID\nChange ID\nChange ID\nChange ID\nChange ID\nChange ID\nChange ID\nChange ID\nChange ID\nChange ID\nChange ID\nChange ID");
    }
    */

  public Set<Place> getPlace() {
    return places;
  }

  public void setPlace(Set<Place> places) {
    for (Place place : places) {
      this.places.add(place);
      place.setService(this);
    }
  }

  public Set<Note> getNote() {
    return notes;
  }

  public void setNote(Set<Note> notes) {
    for (Note note : notes) {
      this.notes.add(note);
      note.setService(this);
    }
  }

  public ServiceOrder getServiceOrder() {
    return serviceOrder;
  }

  public void setServiceOrder(ServiceOrder serviceOrder) {
    this.serviceOrder = serviceOrder;
  }

  public Set<RelatedParty> getRelatedParty() {
    return relatedParties;
  }

  public Set<ServiceCharacteristic> getServiceCharacteristic() {
    return serviceCharacteristics;
  }

  public void setServiceCharacteristic(Set<ServiceCharacteristic> serviceCharacteristics) {
    for (ServiceCharacteristic serviceCharacteristic : serviceCharacteristics) {
      this.serviceCharacteristics.add(serviceCharacteristic);
      serviceCharacteristic.setService(this);

    }
  }

  public Set<ServiceRelationship> getServiceRelationship() {
    return serviceRelationships;
  }

  public void setServiceRelationship(Set<ServiceRelationship> serviceRelationships) {
    for (ServiceRelationship serviceRelationship : serviceRelationships) {
      this.serviceRelationships.add(serviceRelationship);
      serviceRelationship.setService(this);
    }
  }

  public List<SupportingResource> getSupportingResource() {
    return supportingResources;
  }

  public List<SupportingService> getSupportingService() {
    return supportingServices;
  }
}
