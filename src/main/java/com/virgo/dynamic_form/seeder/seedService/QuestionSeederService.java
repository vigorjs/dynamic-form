package com.virgo.dynamic_form.seeder.seedService;

import com.virgo.dynamic_form.dto.request.QuestionRequestDTO;
import com.virgo.dynamic_form.model.enums.ChoiceType;
import com.virgo.dynamic_form.service.QuestionService;
import com.virgo.dynamic_form.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionSeederService {

    private final QuestionService questionService;
    private final UserService userService;

    public void seederQuestion() {
        UserDetails userDetails = userService.loadUserByUsername("user1@webtech.id");
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        List<QuestionRequestDTO> questions = new ArrayList<>();
        questions.add(new QuestionRequestDTO("Name", ChoiceType.SHORT_ANSWER, null, true));
        questions.add(new QuestionRequestDTO("Address", ChoiceType.SHORT_ANSWER, null, true));
        questions.add(new QuestionRequestDTO("Born Date", ChoiceType.DATE, null, true));
        questions.add(new QuestionRequestDTO("Sex", ChoiceType.SHORT_ANSWER, null, true));

        for (var question : questions) {
            questionService.addQuestion("biodata", question);
        }

        SecurityContextHolder.clearContext();
        System.out.println("Question seeder successfully executed");
    }
}
