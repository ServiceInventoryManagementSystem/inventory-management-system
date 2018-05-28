package org.sims.repository;

import org.sims.model.SupportingResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportingResourceRepository extends JpaRepository<SupportingResource, Long> {


}
