package org.sims.repository;

import com.querydsl.core.types.dsl.StringPath;
import org.sims.model.QService;
import org.sims.model.QSupportingService;
import org.sims.model.Service;
import org.sims.model.SupportingService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportingServiceRepository extends JpaRepository<SupportingService, Long>,
        QuerydslPredicateExecutor<SupportingService>, QuerydslBinderCustomizer<QSupportingService> {
  @Override
  default public void customize(QuerydslBindings bindings, QSupportingService root) {
    bindings.bind(String.class).first(
            (StringPath path, String value) -> path.containsIgnoreCase(value));
  }

}
