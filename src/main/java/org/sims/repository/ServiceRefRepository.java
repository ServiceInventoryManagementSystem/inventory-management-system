package org.sims.repository;

import com.querydsl.core.types.dsl.StringPath;
import org.sims.model.QServiceRef;
import org.sims.model.ServiceRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRefRepository extends JpaRepository<ServiceRef, Long>, QuerydslPredicateExecutor<ServiceRef>, QuerydslBinderCustomizer<QServiceRef> {
  @Override
  default public void customize(QuerydslBindings bindings, QServiceRef root) {
    bindings.bind(String.class).first(
            (StringPath path, String value) -> path.containsIgnoreCase(value));
  }

}
