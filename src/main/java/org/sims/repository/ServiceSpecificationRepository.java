package org.sims.repository;

import org.sims.model.ServiceSpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceSpecificationRepository extends JpaRepository<ServiceSpecification, Long> {

}
