package org.sims.repository;

import org.sims.model.Hub;
import org.sims.model.SpecificResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecificResourceRepository extends JpaRepository<SpecificResource, String> {
}
