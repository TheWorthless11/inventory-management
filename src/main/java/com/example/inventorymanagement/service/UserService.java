package com.example.inventorymanagement.service;


import com.example.inventorymanagement.entity.Users;
import com.example.inventorymanagement.exception.ResourceNotFoundException;
import com.example.inventorymanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository; // service talks with repo for sql query actions and repo talks with entity/database
    private final PasswordEncoder passwordEncoder;

    @Autowired //connects service with repo
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {

        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
    }

    public Users registerUser(Users user){
        // Check if username is already taken
        if(userRepository.findByUsername(user.getUsername()).isPresent()){
            throw new IllegalArgumentException(("Username already exists!"));
        }
        // Check if role is valid (Defensive Programming)
        String role= user.getRole().toUpperCase();
        if(!role.equals("ADMIN") && !role.equals("SELLER") && !role.equals("BUYER")){
            throw new IllegalArgumentException("Invalid role! Must be 'ADMIN' or 'SELLER' or 'BUYER'.");

        }
        user.setRole(role); // set role

        // REQUIREMENT 1 MET: Encrypt the password using BCrypt!
        String scambledPassword = passwordEncoder.encode(user.getPassword()); //take user password and then encrypt it
        user.setPassword(scambledPassword);//after encryption set the password

        return userRepository.save(user); //save the user
    }

    // 2. FIND USER BY USERNAME (Used later for Login)
    public Users getUserByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(
                ()->new ResourceNotFoundException("User with username '" + username + "' not found!")
        );
    }


}
