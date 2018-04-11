package org.sims.repository;

import com.querydsl.core.types.dsl.StringPath;
import org.sims.model.Note;
import org.sims.model.QNote;
import org.sims.model.QService;
import org.sims.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long>, QuerydslPredicateExecutor<Note>, QuerydslBinderCustomizer<QNote> {
  @Override
  default public void customize(QuerydslBindings bindings, QNote root) {
    bindings.bind(String.class).first(
            (StringPath path, String value) -> path.containsIgnoreCase(value));
  }
}
