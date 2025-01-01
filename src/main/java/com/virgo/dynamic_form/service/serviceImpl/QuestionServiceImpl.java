package com.virgo.dynamic_form.service.serviceImpl;

import com.virgo.dynamic_form.config.advisers.exception.InvalidRequestException;
import com.virgo.dynamic_form.dto.request.QuestionRequestDTO;
import com.virgo.dynamic_form.dto.response.QuestionResponseDTO;
import com.virgo.dynamic_form.model.enums.ChoiceType;
import com.virgo.dynamic_form.model.meta.Form;
import com.virgo.dynamic_form.model.meta.Question;
import com.virgo.dynamic_form.repository.FormRepository;
import com.virgo.dynamic_form.repository.QuestionRepository;
import com.virgo.dynamic_form.service.FormService;
import com.virgo.dynamic_form.service.QuestionService;
import com.virgo.dynamic_form.config.advisers.exception.ForbiddenAccessException;
import com.virgo.dynamic_form.config.advisers.exception.NotFoundException;
import com.virgo.dynamic_form.utils.mapper.QuestionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final FormRepository formRepository;
    private final FormService formService;
    private final QuestionMapper questionMapper;

    @Override
    @Transactional
    public QuestionResponseDTO addQuestion(String formSlug, QuestionRequestDTO request) {
        formService.validateFormAccess(formSlug);

        Form form = formRepository.findBySlug(formSlug)
                .orElseThrow(() -> new NotFoundException("Form not found"));

        // validasi untuk tipe question yang butuh ada choicesnya
        if (needsChoices(request.getChoice_type())) {
            if (request.getChoices() == null || request.getChoices().isEmpty()) {
                throw new InvalidRequestException("Choices are required for " + request.getChoice_type());
            }
            if (request.getChoices().size() < 2) {
                throw new InvalidRequestException("At least 2 choices are required for " + request.getChoice_type());
            }
        }

        Question question = questionMapper.toEntity(request);
        question.setForm(form);

        question = questionRepository.save(question);
        return questionMapper.toDTO(question);
    }

    @Override
    @Transactional
    public void removeQuestion(String formSlug, Long questionId) {
        formService.validateFormAccess(formSlug);
        validateQuestionAccess(formSlug, questionId);

        questionRepository.deleteById(questionId);
    }

    @Override
    public void validateQuestionAccess(String formSlug, Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("Question not found"));

        if (!question.getForm().getSlug().equals(formSlug)) {
            throw new ForbiddenAccessException();
        }
    }

    private boolean needsChoices(ChoiceType choiceType) {
        return choiceType == ChoiceType.MULTIPLE_CHOICE ||
                choiceType == ChoiceType.DROPDOWN ||
                choiceType == ChoiceType.CHECKBOXES;
    }
}
