package com.virgo.dynamic_form.seeder;

import com.virgo.dynamic_form.seeder.seedService.FormSeederService;
import com.virgo.dynamic_form.seeder.seedService.QuestionSeederService;
import com.virgo.dynamic_form.seeder.seedService.UsersSeederService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RunnerSeeder {

    @Value("${seed.database}")
    private Boolean seedDatabase;

    private final UsersSeederService usersSeederService;
    private final FormSeederService formSeederService;
    private final QuestionSeederService questionSeederService;


    @PostConstruct
    public void runSeeders() {
        if (!seedDatabase) {
            return;
        }

        usersSeederService.seederUser();
        formSeederService.seederForms();
        questionSeederService.seederQuestion();
    }
}
