package org.sims.repository;

import org.sims.model.ServiceCharacteristic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceCharacteristicRepository extends JpaRepository<ServiceCharacteristic, Long> {


}
