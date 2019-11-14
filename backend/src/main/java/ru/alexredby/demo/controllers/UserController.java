package ru.alexredby.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    /**
     * Gives all users from db with status code OK and with status NoContent if no one user was found
     *
     * @return list of users
     */
    // TODO: add paging
    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userDataService.findAll();
        if(users.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(users);
    }

    /**
     * Update user info from Steam by given Steam id
     *
     * @param userId user Steam id to update
     * @return updated user
     */
    @PostMapping("update/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable(value = "userId", required = true) String userId) {
        // TODO: make service with SteamWebApiClient inside and use it here

        // TODO: replace to proper code
        return ResponseEntity.ok(new User());
    }
}
