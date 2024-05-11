package com.example.demo.User;

import com.example.demo.Auth.AuthResponse;
import com.example.demo.DTO.LoginRequest;
import com.example.demo.DTO.RegisterRequest;
import com.example.demo.DTO.TokenRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping()
    public List<User> getUsers() {
        return userRepository.getUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") int id){
        return userRepository.getUser(id);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody RegisterRequest registerRequest){
        try {
            AuthResponse response = userRepository.registerUser(registerRequest.getEmail(), registerRequest.getName(), registerRequest.getPassword());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new AuthResponse(false, "Error registering user: " + e.getMessage()));
        }
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
       AuthResponse response =  userRepository.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(response);
    }
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody TokenRequest tokenRequest){
        AuthResponse response = userRepository.refreshToken(tokenRequest.getToken());
        return ResponseEntity.ok(response);
    }
}
