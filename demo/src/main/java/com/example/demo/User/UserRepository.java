package com.example.demo.User;

import com.example.demo.Auth.AuthResponse;
import com.example.demo.Auth.JwtGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class UserRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<User> getUsers() {
        return jdbcTemplate.query("SELECT id, email, name FROM USERS", BeanPropertyRowMapper.newInstance(User.class));
    }
    public User getUser(int id){
        return jdbcTemplate.queryForObject("SELECT id, email, name FROM USERS WHERE id = ?", BeanPropertyRowMapper.newInstance(User.class), id);
    }

    public AuthResponse registerUser(String email, String name, String password){
        jdbcTemplate.update("INSERT INTO USERS (email, name, password) VALUES (?, ?, ?)", email, name, password);
        return new AuthResponse(true, "");
    }

    public AuthResponse loginUser(String email, String password){
        User user = jdbcTemplate.queryForObject("SELECT id, email, name, password FROM USERS WHERE email = ?", BeanPropertyRowMapper.newInstance(User.class), email);
        assert Objects.requireNonNull(user).getPassword() != null;
        String token = "";
        boolean success = false;
        if(user.getPassword().equals(password)) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("name", user.getName());
            claims.put("email", user.getEmail());
            claims.put("id", user.getId());
            token = JwtGenerator.generateToken(claims, Integer.toString(user.getId()), 3600000);
            jdbcTemplate.update("UPDATE USERS SET token = ? WHERE id = ?", token, user.getId());
            success = true;
        }
        return new AuthResponse(success, token);
    }
    public AuthResponse refreshToken(String token){
        System.out.println("Received token: " + token);

        try {
            // Attempt to fetch the user based on the provided token
            User user = jdbcTemplate.queryForObject(
                    "SELECT id, email, name FROM USERS WHERE token = ?",
                    new BeanPropertyRowMapper<>(User.class),
                    token
            );

            System.out.println("User retrieved: " + user);

            if(user == null){
                System.out.println("User not found for token: " + token);
                return new AuthResponse(false, "");
            } else {
                // Prepare claims for the new token
                Map<String, Object> claims = new HashMap<>();
                claims.put("name", user.getName());
                claims.put("email", user.getEmail());
                claims.put("id", user.getId());

                // Generate a new access token
                String accessToken = JwtGenerator.generateToken(claims, Integer.toString(user.getId()), 15000);
                return new AuthResponse(true, accessToken);
            }
        } catch (EmptyResultDataAccessException e) {
            // Handle case where no user is found
            System.err.println("No user found for token: " + token);
            return new AuthResponse(false, "");
        } catch (DataAccessException e) {
            // Handle general database access issues
            e.printStackTrace();
            return new AuthResponse(false, "Database error occurred");
        } catch (Exception e) {
            // Handle any other exceptions
            e.printStackTrace();
            return new AuthResponse(false, "An error occurred");
        }

    }
}
