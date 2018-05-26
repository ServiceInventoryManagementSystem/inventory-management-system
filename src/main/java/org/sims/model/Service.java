package org.sims.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@JsonFilter("service")
public class Service implements Serializable {
  @ApiModelProperty(notes="'id' is the ID created for the service.")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;

  private String uuid;

  @JsonIgnore
  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  @ApiModelProperty(notes="Is it a customer facing or resource facing service.")
  private String category;
  @ApiModelProperty(notes="free-text description of the service.")
  @Type(type="text")
  private String description;

  @ApiModelProperty(notes="endDate is the date when the service ends.")
  private OffsetDateTime endDate;

  @ApiModelProperty(notes="This is a Boolean attribute that, if TRUE, signifies that this Service has already been started. If the value of this attribute is FALSE, then this signifies that this Service has NOT been Started.")
  private Boolean hasStarted;
  @ApiModelProperty(notes="Reference of the service.")
  private String href;
  @ApiModelProperty(notes="If the value of this attribute is FALSE, then this means that this particular Service has NOT been enabled for use.")
  private Boolean isServiceEnabled;
  @ApiModelProperty(notes="This is a Boolean attribute that, if TRUE, means that this Service can be changed without affecting any other services.")
  private Boolean isStateful;
  @ApiModelProperty(notes="'Name' is the name of the service.")
  private String name;
  @ApiModelProperty(notes="orderDate is the date when the service was ordered.")
  private OffsetDateTime orderDate;
  @ApiModelProperty(notes="startDate is the date when the service starts.")
  private OffsetDateTime startDate;
  @ApiModelProperty(notes="This attribute is an enumerated integer that indicates how the Service is started: 0: Unknown, 1: Automaically by the managed environment, 2: Automatically by the owner device, 3: Manually by the Provider of the Service, 4: Manually by a Customer of the Provider, 5: Any of the above.")
  private String startMode;
  @ApiModelProperty(notes="The lifecycle state of the service. feasibilityChecked, designed, reserved, active, inactive, terminated.")
  private String state;
  @ApiModelProperty(notes="Name of the resource type.")
  private String type;

  //---------------------------------------Relations------------------------------------------------------------------
  //---------------------------------------OneToOne-------------------------------------------------------------------

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  private ServiceSpecification serviceSpecification;


  //--------------------------------------OneToMany-------------------------------------------------------------------
  @JsonManagedReference
  @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Note> notes = new HashSet<>();

  @JsonManagedReference
  @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Place> places = new HashSet<>();

  @JsonManagedReference
  @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ServiceCharacteristic> serviceCharacteristics = new ArrayList<>();

  @JsonManagedReference
  @OneToMany(mappedBy = "owningService", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<ServiceRelationship> serviceRelationships = new HashSet<>();


  //----------------------------------------ManyToOne-----------------------------------------------------------------

  @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE})
  private ServiceOrder serviceOrder;


  //----------------------------------------ManyToMany----------------------------------------------------------------

  @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(name = "SERVICE_RELATED_PARTY",
          joinColumns = @JoinColumn(name = "SERVICE_ID"),
          inverseJoinColumns = @JoinColumn(name = "RELATED_PARTY_ID"))
  private Set<RelatedParty> relatedParties = new HashSet<>();

  @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(name = "SERVICE_SUPPORTING_RESOURCE",
          joinColumns = @JoinColumn(name = "SERVICE_ID"),
          inverseJoinColumns = @JoinColumn(name = "SUPPORTING_RESOURCE_ID"))
  private List<SupportingResource> supportingResources = new ArrayList<>();

  @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(name = "SERVICE_SUPPORTING_SERVICE",
          joinColumns = @JoinColumn(name = "SERVICE_ID"),
          inverseJoinColumns = @JoinColumn(name = "SUPPORTING_SERVICE_ID"))
  private List<SupportingService> supportingServices = new ArrayList<>();


  //-----------------------------------------Constructor--------------------------------------------------------------

  public Service() {}


  //-----------------------------Getters and Setters for non-relations------------------------------------------------

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

  public OffsetDateTime getEndDate() {
    return endDate;
  }

  public void setEndDate(OffsetDateTime endDate) {
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

  @JsonProperty("isServiceEnabled")
  public Boolean getServiceEnabled() {
    return isServiceEnabled;
  }
  @JsonProperty("isServiceEnabled")
  public void setServiceEnabled(Boolean serviceEnabled) {
    isServiceEnabled = serviceEnabled;
  }

  @JsonProperty("isStateful")
  public Boolean getStateful() {
    return isStateful;
  }
  @JsonProperty("isStateful")
  public void setStateful(Boolean stateful) {
    isStateful = stateful;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public OffsetDateTime getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(OffsetDateTime orderDate) {
    this.orderDate = orderDate;
  }

  public OffsetDateTime getStartDate() {
    return startDate;
  }

  public void setStartDate(OffsetDateTime startDate) {
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

  @JsonProperty("note")
  public Set<Note> getNotes() {
    return notes;
  }
  @JsonProperty("note")
  public void setNotes(Set<Note> notes) {
    this.notes = notes;
  }

  @JsonProperty("place")
  public Set<Place> getPlaces() {
    return places;
  }
  @JsonProperty("place")
  public void setPlaces(Set<Place> places) {
    this.places = places;
  }

  @JsonProperty("serviceCharacteristic")
  public List<ServiceCharacteristic> getServiceCharacteristics() {
    return serviceCharacteristics;
  }
  @JsonProperty("serviceCharacteristic")
  public void setServiceCharacteristics(List<ServiceCharacteristic> serviceCharacteristics) {
    this.serviceCharacteristics = serviceCharacteristics;
  }

  @JsonProperty("serviceRelationship")
  public Set<ServiceRelationship> getServiceRelationships() {
    return serviceRelationships;
  }
  @JsonProperty("serviceRelationship")
  public void setServiceRelationships(Set<ServiceRelationship> serviceRelationships) {
    this.serviceRelationships = serviceRelationships;
  }

  public ServiceOrder getServiceOrder() {
    return serviceOrder;
  }

  public void setServiceOrder(ServiceOrder serviceOrder) {
    this.serviceOrder = serviceOrder;
  }

  @JsonProperty("relatedParty")
  public Set<RelatedParty> getRelatedParties() {
    return relatedParties;
  }
  @JsonProperty("relatedParty")
  public void setRelatedParties(Set<RelatedParty> relatedParties) {
    this.relatedParties = relatedParties;
  }

  @JsonProperty("supportingResource")
  public List<SupportingResource> getSupportingResources() {
    return supportingResources;
  }
  @JsonProperty("supportingResource")
  public void setSupportingResources(List<SupportingResource> supportingResources) {
    this.supportingResources = supportingResources;
  }

  @JsonProperty("supportingService")
  public List<SupportingService> getSupportingServices() {
    return supportingServices;
  }

  @JsonProperty("supportingService")
  public void setSupportingServices(List<SupportingService> supportingServices) {
    this.supportingServices = supportingServices;
  }
}
