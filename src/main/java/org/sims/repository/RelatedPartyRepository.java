package org.sims.repository;

import com.querydsl.core.types.dsl.StringPath;
import org.sims.model.QRelatedParty;
import org.sims.model.QService;
import org.sims.model.RelatedParty;
import org.sims.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface RelatedPartyRepository extends JpaRepository<RelatedParty, Long>, QuerydslPredicateExecutor<RelatedParty>, QuerydslBinderCustomizer<QRelatedParty> {
  @Override
  default public void customize(QuerydslBindings bindings, QRelatedParty root) {
    bindings.bind(String.class).first(
            (StringPath path, String value) -> path.containsIgnoreCase(value));
  }

}
