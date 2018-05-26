//package org.sims.model;
//
//import javax.persistence.*;
//import java.util.HashSet;
//import java.util.Set;
//
//@Entity
//public class SpecificResource {
//  @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
//  private String id;
//
//  @OneToMany(mappedBy = "specificResource", cascade = CascadeType.ALL)
//  private Set<SpecificEvent> specificEvents = new HashSet<>();
//}
