package com.virgo.dynamic_form.service.serviceImpl;

import com.virgo.dynamic_form.dto.request.ResponseRequestDTO;
import com.virgo.dynamic_form.dto.response.ResponseResponseDTO;
import com.virgo.dynamic_form.model.meta.*;
import com.virgo.dynamic_form.repository.AnswerRepository;
import com.virgo.dynamic_form.repository.FormRepository;
import com.virgo.dynamic_form.repository.QuestionRepository;
import com.virgo.dynamic_form.repository.ResponseRepository;
import com.virgo.dynamic_form.service.FormService;
import com.virgo.dynamic_form.service.ResponseService;
import com.virgo.dynamic_form.service.UserService;
import com.virgo.dynamic_form.config.advisers.exception.ForbiddenAccessException;
import com.virgo.dynamic_form.config.advisers.exception.InvalidRequestException;
import com.virgo.dynamic_form.config.advisers.exception.NotFoundException;
import com.virgo.dynamic_form.utils.mapper.AnswerMapper;
import com.virgo.dynamic_form.utils.mapper.ResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResponseServiceImpl implements ResponseService {
    private final ResponseRepository responseRepository;
    private final FormRepository formRepository;
    private final FormService formService;
    private final UserService userService;
    private final ResponseMapper responseMapper;
    private final AnswerRepository answerRepository;
    private final AnswerMapper answerMapper;
    private final QuestionRepository questionRepository;

    @Override
    @Transactional
    public void submitResponse(String formSlug, ResponseRequestDTO request) {
        UserEntity user = userService.getUserAuthenticated();
        Form form = formRepository.findBySlug(formSlug)
                .orElseThrow(() -> new NotFoundException("Form not found"));

        if (!formService.isUserAllowedToSubmit(formSlug, user.getEmail())) {
            throw new ForbiddenAccessException("You are not allowed to submit this form");
        }

        if (Boolean.TRUE.equals(form.getLimitOneResponse()) && hasUserSubmitted(formSlug, user.getId())) {
            throw new InvalidRequestException("You can not submit form twice");
        }

        validateRequiredQuestions(form, request);

        Response response = Response.builder()
                .form(form)
                .user(user)
                .build();

        responseRepository.save(response);

        List<Answer> answers = request.getAnswers().stream()
                .map(answerReqDTO -> {
                    Question question = questionRepository.findById(answerReqDTO.getQuestion_id())
                            .orElseThrow(() -> new NotFoundException("Question Not Found"));
                    return answerMapper.toEntity(answerReqDTO, response, question);
                })
                .collect(Collectors.toList());

        answerRepository.saveAll(answers);
    }

    @Override
    @Transactional
    public List<ResponseResponseDTO> getAllResponses(String formSlug) {
        formService.validateFormAccess(formSlug);

        List<ResponseResponseDTO> res = responseRepository.findAllByFormSlugWithAnswersAndUser(formSlug).stream()
                .map(responseMapper::toDTO)
                .toList();

        return res;
    }

    @Override
    public boolean hasUserSubmitted(String formSlug, String userId) {
        return responseRepository.existsByFormSlugAndUserId(formSlug, userId);
    }

    private void validateRequiredQuestions(Form form, ResponseRequestDTO request) {
        form.getQuestions().stream()
                .filter(Question::getIsRequired)
                .forEach(question -> {
                    Long question_id = request.getAnswers().stream()
                            .map(ResponseRequestDTO.AnswerRequestDTO::getQuestion_id)
                            .filter(questionId -> questionId == question.getId())
                            .findFirst()
                            .orElse(null);

                    if (question_id == null) {
                        throw new InvalidRequestException("Question '" + question.getName() + "' is required");
                    }
                });
    }
}