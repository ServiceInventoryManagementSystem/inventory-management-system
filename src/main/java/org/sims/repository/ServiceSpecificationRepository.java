package org.sims.repository;

import com.querydsl.core.types.dsl.StringPath;
import org.sims.model.QServiceSpecification;
import org.sims.model.ServiceSpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceSpecificationRepository extends JpaRepository<ServiceSpecification, Long>,
        QuerydslPredicateExecutor<ServiceSpecification>, QuerydslBinderCustomizer<QServiceSpecification> {
  @Override
  default public void customize(QuerydslBindings bindings, QServiceSpecification root) {
    bindings.bind(String.class).first(
            (StringPath path, String value) -> path.containsIgnoreCase(value));
  }

}
