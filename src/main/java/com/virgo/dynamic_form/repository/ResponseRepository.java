package com.virgo.dynamic_form.repository;

import com.virgo.dynamic_form.model.meta.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResponseRepository extends JpaRepository<Response, String> {
    boolean existsByFormSlugAndUserId(String formSlug, String userId);

    @Query("SELECT r FROM Response r LEFT JOIN FETCH r.answers a LEFT JOIN FETCH a.question LEFT JOIN FETCH r.user WHERE r.form.slug = :formSlug")
    List<Response> findAllByFormSlugWithAnswersAndUser(@Param("formSlug") String formSlug);
}
