package com.example.passworddemo.controller;

import com.example.passworddemo.model.User;
import com.example.passworddemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@RequestBody Map<String, String> userInfo) {
        String name = userInfo.get("name");
        String email = userInfo.get("email");
        String password = userInfo.get("password");
        
        if (name == null || email == null || password == null) {
            return ResponseEntity.badRequest().build();
        }
        
        User newUser = userService.addUser(name, email, password);
        return ResponseEntity.ok(newUser);
    }

    @GetMapping
    public ResponseEntity<List<User>> listUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/mode")
    public ResponseEntity<String> setMode(@RequestBody Map<String, Boolean> modeInfo) {
        Boolean secureMode = modeInfo.get("secureMode");
        
        if (secureMode == null) {
            return ResponseEntity.badRequest().body("Invalid mode specified");
        }
        
        userService.setSecureMode(secureMode);
        return ResponseEntity.ok("Mode set to: " + (secureMode ? "secure" : "insecure"));
    }
}