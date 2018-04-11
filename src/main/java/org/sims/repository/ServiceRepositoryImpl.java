package org.sims.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class ServiceRepositoryImpl implements ServiceRepositoryCustom {
  @PersistenceContext
  private EntityManager em;
}
