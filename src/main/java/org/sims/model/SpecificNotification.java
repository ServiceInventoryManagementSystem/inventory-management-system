package org.sims.model;

import javax.persistence.*;

@Entity
public class SpecificNotification {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "specific_event_id")
  private SpecificEvent specificEvent;
}
