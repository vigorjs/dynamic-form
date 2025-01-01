package com.virgo.dynamic_form.utils.mapper;

import com.virgo.dynamic_form.dto.request.FormRequestDTO;
import com.virgo.dynamic_form.dto.response.FormResponseDTO;
import com.virgo.dynamic_form.dto.response.FormWithQuestionsAndDomainsResonseDTO;
import com.virgo.dynamic_form.model.meta.AllowedDomain;
import com.virgo.dynamic_form.model.meta.Form;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FormMapper {
    private final QuestionMapper questionMapper;

    public FormResponseDTO toDTO(Form form) {
        return FormResponseDTO.builder()
                .id(form.getId())
                .name(form.getName())
                .slug(form.getSlug())
                .description(form.getDescription())
                .limit_one_response(form.getLimitOneResponse())
                .creator_id(form.getCreator().getId())
                .build();
    }

    public FormWithQuestionsAndDomainsResonseDTO toDTOWithQuestionsAndDomains(Form form) {
        return FormWithQuestionsAndDomainsResonseDTO.builder()
                .id(form.getId())
                .name(form.getName())
                .slug(form.getSlug())
                .description(form.getDescription())
                .limit_one_response(form.getLimitOneResponse())
                .creator_id(form.getCreator().getId())
                .allowed_domains(
                        form.getAllowedDomains() == null ?
                                List.of() :
                                form.getAllowedDomains().stream()
                                        .map(AllowedDomain::getDomain)
                                        .toList()
                )
                .questions(
                        form.getQuestions() == null ?
                                List.of() :
                                form.getQuestions().stream()
                                        .map(questionMapper::toDTO)
                                        .toList()
                )
                .build();
    }


    public Form toEntity(FormRequestDTO dto) {
         Form form = Form.builder()
                .name(dto.getName())
                .slug(dto.getSlug())
                .description(dto.getDescription())
                .limitOneResponse(dto.getLimit_one_response())
                .build();
         return form;
    }
}