package org.sims.model;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Hub {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;

  private String callback;

  private String query = null;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getQuery() {
    return query;
  }

  public String getCallback() {
    return callback;
  }

  public void setCallback(String callback) {
    this.callback = callback;
  }

  public void setQuery(String query) {
    this.query = query;
  }
}
