package org.sims.repository;

import com.querydsl.core.types.dsl.StringPath;
import org.sims.model.QServiceRelationship;
import org.sims.model.ServiceRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRelationshipRepository extends JpaRepository<ServiceRelationship, Long>,
        QuerydslPredicateExecutor<ServiceRelationship>, QuerydslBinderCustomizer<QServiceRelationship> {
  @Override
  default public void customize(QuerydslBindings bindings, QServiceRelationship root) {
    bindings.bind(String.class).first(
            (StringPath path, String value) -> path.containsIgnoreCase(value));
  }

}
