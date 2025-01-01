package com.virgo.dynamic_form.repository;

import com.virgo.dynamic_form.model.meta.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FormRepository extends JpaRepository<Form, Long> {
    List<Form> findAllByCreatorId(String creatorId);

    @Query("SELECT f FROM Form f WHERE f.slug = :slug")
    Optional<Form> findBySlug(@Param("slug") String slug);

    boolean existsBySlug(String slug);
}
