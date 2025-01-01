package com.virgo.dynamic_form.service.serviceImpl;

import com.virgo.dynamic_form.config.advisers.exception.DuplicateEntryException;
import com.virgo.dynamic_form.dto.request.FormRequestDTO;
import com.virgo.dynamic_form.dto.response.FormResponseDTO;
import com.virgo.dynamic_form.dto.response.FormWithQuestionsAndDomainsResonseDTO;
import com.virgo.dynamic_form.model.meta.AllowedDomain;
import com.virgo.dynamic_form.model.meta.Form;
import com.virgo.dynamic_form.model.meta.Question;
import com.virgo.dynamic_form.model.meta.UserEntity;
import com.virgo.dynamic_form.repository.AllowedDomainRepository;
import com.virgo.dynamic_form.repository.FormRepository;
import com.virgo.dynamic_form.repository.QuestionRepository;
import com.virgo.dynamic_form.service.FormService;
import com.virgo.dynamic_form.service.UserService;
import com.virgo.dynamic_form.config.advisers.exception.ForbiddenAccessException;
import com.virgo.dynamic_form.config.advisers.exception.NotFoundException;
import com.virgo.dynamic_form.utils.mapper.FormMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FormServiceImpl implements FormService {
    private final FormRepository formRepository;
    private final UserService userService;
    private final FormMapper formMapper;
    private final AllowedDomainRepository allowedDomainRepository;
    private final QuestionRepository questionRepository;

    @Override
    @Transactional
    public FormResponseDTO createForm(FormRequestDTO request) {
        //validasi slug duplicate
        if (formRepository.existsBySlug(request.getSlug())) {
            throw new DuplicateEntryException("Form with this slug already exists");
        }

        UserEntity user = userService.getUserAuthenticated();

        Form form = formMapper.toEntity(request);
        form.setCreator(user);

        if (request.getAllowed_domains() != null && !request.getAllowed_domains().isEmpty()) {
            List<AllowedDomain> allowedDomains = new ArrayList<>();
            for (String domain : request.getAllowed_domains()) {
                AllowedDomain build = AllowedDomain.builder()
                        .domain(domain)
                        .form(form)
                        .build();
                allowedDomains.add(build);
            }
            form.setAllowedDomains(allowedDomains);
        }

        form = formRepository.save(form);
        return formMapper.toDTO(form);
    }

    @Override
    public List<FormResponseDTO> getAllForms() {
        UserEntity user = userService.getUserAuthenticated();
        return formRepository.findAllByCreatorId(user.getId()).stream()
                .map(formMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FormWithQuestionsAndDomainsResonseDTO getFormBySlug(String slug) {
        Form form = formRepository.findBySlug(slug).orElseThrow(() -> new NotFoundException("Form not found"));
        List<Question> questions = questionRepository.findAllByFormId(form.getId());
        List<AllowedDomain> allowed_domains = allowedDomainRepository.findAllByFormId(form.getId());

        form.setQuestions(questions);
        form.setAllowedDomains(allowed_domains);

        return formMapper.toDTOWithQuestionsAndDomains(form);
    }

    @Override
    public boolean isUserAllowedToSubmit(String formSlug, String userEmail) {
        Form form = formRepository.findBySlug(formSlug).orElseThrow(() -> new NotFoundException("Form not found"));
        List<AllowedDomain> allowed_domains = allowedDomainRepository.findAllByFormId(form.getId());

        if (allowed_domains.isEmpty()) {
            return true;
        }

        //ambil domain dari email user
        String userDomain = userEmail.substring(userEmail.indexOf("@") + 1);

        return form.getAllowedDomains().stream()
                .anyMatch(domain -> domain.getDomain().equals(userDomain));
    }

    @Override
    @Transactional
    public void updateFormDomains(String formSlug, List<String> domains) {
        Form form = formRepository.findBySlug(formSlug)
                .orElseThrow(() -> new NotFoundException("Form not found"));

        // delete existing domains
        allowedDomainRepository.deleteAllByFormId(form.getId());

        if (domains != null && !domains.isEmpty()) {
            List<AllowedDomain> allowedDomains = domains.stream()
                    .map(domain -> AllowedDomain.builder()
                            .domain(domain)
                            .form(form)
                            .build())
                    .collect(Collectors.toList());

            allowedDomainRepository.saveAll(allowedDomains);
        }
    }

    @Override
    public void validateFormAccess(String formSlug) {
        Form form = formRepository.findBySlug(formSlug)
                .orElseThrow(() -> new NotFoundException("Form not found"));
        UserEntity user = userService.getUserAuthenticated();

        if (!form.getCreator().getId().equals(user.getId())) {
            throw new ForbiddenAccessException();
        }
    }
}