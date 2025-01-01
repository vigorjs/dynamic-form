package com.virgo.dynamic_form.repository;

import com.virgo.dynamic_form.model.meta.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByFormId(Long formId);
    void deleteAllByFormId(Long formId);
}
