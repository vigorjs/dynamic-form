//package com.virgo.dynamic_form.seeder;
//
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class RunnerSeeder {
//
//    @Value("${seed.database}")
//    private Boolean seedDatabase;
//
//    private final UsersSeederService usersSeederService;
//    private final TalentDataSeeder talentDataSeeder;
//
//
//    @PostConstruct
//    public void runSeeders() {
//        if (!seedDatabase) {
//            return;
//        }
//
//        usersSeederService.seederAdmin();
//        usersSeederService.seederTalent();
//        usersSeederService.seederClient();
//        usersSeederService.verifiedClient();
//        usersSeederService.seederSkill();
//        usersSeederService.seederStaff();
//        usersSeederService.talentSkillSeeder();
//        usersSeederService.assessmentSeeder();
//        usersSeederService.seederScore();
//        talentDataSeeder.seederRecommendation();
//        talentDataSeeder.seederCertification();
//        talentDataSeeder.seederExperience();
//        talentDataSeeder.seederProject();
//        talentDataSeeder.seederSocial();
//        talentDataSeeder.seederEducation();
//        talentDataSeeder.seedTalentPlacement();
//
//    }
//}
