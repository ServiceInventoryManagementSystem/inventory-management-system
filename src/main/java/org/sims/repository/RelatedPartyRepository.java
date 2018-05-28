package org.sims.repository;

import org.sims.model.RelatedParty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelatedPartyRepository extends JpaRepository<RelatedParty, Long> {
}
