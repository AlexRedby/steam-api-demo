package ru.alexredby.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.alexredby.demo.persistance.models.User;
import ru.alexredby.demo.persistance.services.UserDataService;
import ru.alexredby.demo.services.SteamExternalDataService;

import java.util.List;

@RestController
@RequestMapping(path = "users", consumes = "application/json")
public class UserController {

    private UserDataService userDataService;

    private SteamExternalDataService steamExternalDataService;

    @Autowired
    public UserController(UserDataService userDataService, SteamExternalDataService steamExternalDataService) {
        this.userDataService = userDataService;
        this.steamExternalDataService = steamExternalDataService;
    }

    /**
     * Gives all users from db with status code OK and with status NoContent if no one user was found
     *
     * @return a list of users
     */
    // TODO: add paging
    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userDataService.findAll();
        if(users.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(users);
    }

    /**
     * Update user info from Steam Api by given Steam id
     *
     * @param userId a user's Steam id to update
     * @return a updated user
     */
    @PostMapping("update/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable(value = "userId", required = true) long userId) {
        return ResponseEntity.ok(steamExternalDataService.updateOrCreateUser(userId));
    }
}
