package org.sims.model.filter;

import com.fasterxml.jackson.annotation.JsonFilter;
import io.swagger.annotations.ApiModelProperty;
import org.sims.model.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//@Entity
@JsonFilter("serviceFilter")
public class ServiceFilter {

  private String id;

  public void setId(String id) {
    this.id = id;
  }

  private String category;
  private String description;
  private String endDate;
  private Boolean hasStarted;
  private String href;
  private Boolean isServiceEnabled;
  private Boolean isStateful;
  private String name;
  private String orderDate;
  private String startDate;
  private String startMode;
  private String state;
  private String type;

  //---------------------------------------Relations------------------------------------------------------------------
  //---------------------------------------OneToOne-------------------------------------------------------------------

  private ServiceSpecification serviceSpecification;


  //--------------------------------------OneToMany-------------------------------------------------------------------
  private Set<Note> notes = new HashSet<>();
  private Set<Place> places = new HashSet<>();
  private Set<ServiceCharacteristic> serviceCharacteristics = new HashSet<>();
  private Set<ServiceRelationship> serviceRelationships = new HashSet<>();

  //----------------------------------------ManyToOne-----------------------------------------------------------------

  private ServiceOrder serviceOrder;


  //----------------------------------------ManyToMany----------------------------------------------------------------

  private Set<RelatedParty> relatedParties = new HashSet<>();
  private List<SupportingResource> supportingResources = new ArrayList<>();
  private List<SupportingService> supportingServices = new ArrayList<>();


  //-----------------------------------------Constructor--------------------------------------------------------------

  public ServiceFilter() {
  }


  //-----------------------------Getters and Setters for non-relations------------------------------------------------

  public String getId() {
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

  public Set<Place> getPlace() {
    return places;
  }

  public void setPlace(Set<Place> places) {
    this.places = places;
  }

  public Set<Note> getNote() {
    return notes;
  }

  public void setNote(Set<Note> notes) {
    this.notes = notes;
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

  public void setRelatedParty(Set<RelatedParty> relatedParty) { this.relatedParties = relatedParty; }

  public Set<ServiceCharacteristic> getServiceCharacteristic() {
    return serviceCharacteristics;
  }

  public void setServiceCharacteristic(Set<ServiceCharacteristic> serviceCharacteristics) {
    this.serviceCharacteristics = serviceCharacteristics;
  }

  public Set<ServiceRelationship> getServiceRelationship() {
    return serviceRelationships;
  }

  public void setServiceRelationship(Set<ServiceRelationship> serviceRelationships) {
    this.serviceRelationships = serviceRelationships;
  }

  public List<SupportingResource> getSupportingResource() {
    return supportingResources;
  }

  public void setSupportingResource(List<SupportingResource> supportingResource) {
    this.supportingResources = supportingResource;
  }

  public List<SupportingService> getSupportingService() {
    return supportingServices;
  }

  public void setSupportingService(List<SupportingService> supportingService) {
    this.supportingServices = supportingService;
  }
}
