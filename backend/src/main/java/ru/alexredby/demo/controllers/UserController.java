package ru.alexredby.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.alexredby.demo.persistance.models.User;
import ru.alexredby.demo.persistance.services.UserDataService;

import java.util.List;

@RestController
@RequestMapping(path = "users", consumes = "application/json")
public class UserController {

    private UserDataService userDataService;

    @Autowired
    public UserController(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userDataService.findAll();
        if(users.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(users);
    }
}
