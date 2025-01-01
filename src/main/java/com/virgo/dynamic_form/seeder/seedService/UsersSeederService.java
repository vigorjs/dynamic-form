package com.virgo.dynamic_form.seeder.seedService;

import com.virgo.dynamic_form.dto.request.RegisterRequestDTO;
import com.virgo.dynamic_form.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsersSeederService {
    private final AuthService authService;

    public void seederUser() {
        List<RegisterRequestDTO> users = new ArrayList<>();
        users.add(new RegisterRequestDTO("User 1", "user1@webtech.id", "password1"));
        users.add(new RegisterRequestDTO("User 2", "user2@webtech.id", "password2"));
        users.add(new RegisterRequestDTO("User 3", "user3@worldskills.org", "password3"));

        for (var user : users) {
                authService.register(user);
        }
        System.out.println("User seeder successfully executed");
    }
}
