package org.sims.repository;

import com.querydsl.core.types.dsl.StringPath;
import org.sims.model.QServiceOrder;
import org.sims.model.ServiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Long>,
        QuerydslPredicateExecutor<ServiceOrder>, QuerydslBinderCustomizer<QServiceOrder> {
  @Override
  default public void customize(QuerydslBindings bindings, QServiceOrder root) {
    bindings.bind(String.class).first(
            (StringPath path, String value) -> path.containsIgnoreCase(value));
  }

}
