package org.sims.demo.repository;

import com.querydsl.core.types.dsl.StringPath;
import org.sims.demo.model.Place;
import org.sims.demo.model.QPlace;
import org.sims.demo.model.QServiceSpecification;
import org.sims.demo.model.ServiceSpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long>, QuerydslPredicateExecutor<Place>, QuerydslBinderCustomizer<QPlace> {
    @Override
    default public void customize(QuerydslBindings bindings, QPlace root) {
        bindings.bind(String.class).first(
                (StringPath path, String value) -> path.containsIgnoreCase(value));
    }
}
