package org.sims.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Notification {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String eventId;
  private String eventTime;
  private String eventType;

  private String fieldPath;

  private String resourcePath;


}
