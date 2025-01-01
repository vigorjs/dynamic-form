package com.virgo.dynamic_form.seeder.seedService;

import com.virgo.dynamic_form.config.advisers.exception.NotFoundException;
import com.virgo.dynamic_form.model.meta.Form;
import com.virgo.dynamic_form.model.meta.UserEntity;
import com.virgo.dynamic_form.repository.FormRepository;
import com.virgo.dynamic_form.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FormSeederService {

    private final FormRepository formRepository;
    private final UserRepository userRepository;

    public void seederForms() {
        UserEntity creator = userRepository.findByEmail("user1@webtech.id")
                .orElseThrow(() -> new NotFoundException("Creator user not found"));

        List<Form> forms = new ArrayList<>();

        forms.add(Form.builder()
                .name("Biodata - Web Tech Members")
                .slug("biodata")
                .description("To save web tech member biodata")
                .limitOneResponse(true)
                .creator(creator)
                .build());

        forms.add(Form.builder()
                .name("HTML and CSS Skills - Quiz")
                .slug("htmlcss-quiz")
                .description("Fundamental web tests")
                .limitOneResponse(true)
                .creator(creator)
                .build());

        formRepository.saveAll(forms);
        System.out.println("Form seeder successfully executed");
    }
}