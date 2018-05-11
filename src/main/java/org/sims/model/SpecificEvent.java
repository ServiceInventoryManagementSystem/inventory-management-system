package org.sims.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class SpecificEvent {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "specific_resource_id")
  private SpecificResource specificResource;

  @OneToMany(mappedBy = "specificEvent", cascade = CascadeType.ALL)
  private Set<SpecificNotification> specificNotifications = new HashSet<>();
}
