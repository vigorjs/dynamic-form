package com.virgo.dynamic_form.repository;

import com.virgo.dynamic_form.model.meta.AllowedDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AllowedDomainRepository extends JpaRepository<AllowedDomain, String> {
    List<AllowedDomain> findAllByFormId(Long formId);
    void deleteAllByFormId(Long formId);
}
