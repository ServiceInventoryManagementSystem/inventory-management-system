package org.sims.repository;

import org.sims.model.ServiceRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRefRepository extends JpaRepository<ServiceRef, Long> {

}
