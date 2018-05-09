package org.sims.repository;

import com.querydsl.core.types.dsl.StringPath;
import org.sims.model.QSupportingResource;
import org.sims.model.SupportingResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportingResourceRepository extends JpaRepository<SupportingResource, Long>,
        QuerydslPredicateExecutor<SupportingResource>, QuerydslBinderCustomizer<QSupportingResource> {
  @Override
  default public void customize(QuerydslBindings bindings, QSupportingResource root) {
    bindings.bind(String.class).first(
            (StringPath path, String value) -> path.containsIgnoreCase(value));
  }

}
