package org.sims.repository;

import org.sims.model.Hub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HubRepository extends JpaRepository<Hub, String> {
}
