package org.sims.repository;

import com.querydsl.core.types.dsl.StringPath;
import org.sims.model.QService;
import org.sims.model.QServiceCharacteristic;
import org.sims.model.Service;
import org.sims.model.ServiceCharacteristic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceCharacteristicRepository extends JpaRepository<ServiceCharacteristic, Long>,
        QuerydslPredicateExecutor<ServiceCharacteristic>, QuerydslBinderCustomizer<QServiceCharacteristic> {
  @Override
  default public void customize(QuerydslBindings bindings, QServiceCharacteristic root) {
    bindings.bind(String.class).first(
            (StringPath path, String value) -> path.containsIgnoreCase(value));
  }

}
