package org.sims.repository;

import org.sims.model.Hub;
import org.sims.model.SpecificNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecificNotificationRepository extends JpaRepository<SpecificNotification, String> {
}
