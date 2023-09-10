package com.apostolis.spms.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apostolis.spms.model.LoginDTO;
import com.apostolis.spms.model.RegisterDTO;
import com.apostolis.spms.model.User;

import com.apostolis.spms.repository.UserRepository;
import com.apostolis.spms.security.JwtGeneratorValidator;
import com.apostolis.spms.service.AppUserDetailsService;
import com.apostolis.spms.model.CustomHttpResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

	@Autowired
	JwtGeneratorValidator jwtGenVal;
	
	@Autowired
	AppUserDetailsService userService;

    // User sign in to system
    @PostMapping("/signin")
    public ResponseEntity<Object> authenticateUser(@RequestBody LoginDTO loginDTO){
        try{
            Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrEmail(), loginDTO.getPassword()));
            
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Give to the successfully signed in user a JWT Token. 
            String jwtToken = jwtGenVal.generateToken(authentication);
            
            Map<String, String> response = new HashMap<>();
            response.put("JWT Token",jwtToken);
            return CustomHttpResponse.generateRespose("User signed in succesfully", HttpStatus.ACCEPTED, response);
            
        }catch(Exception e){
            return CustomHttpResponse.generateRespose(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, "Could not sign in");
        }
    }

    // Register new user
    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody RegisterDTO registerDTO){
        try{
            // Check if username exists in a DB
            if(userRepository.existsByUsername(registerDTO.getUsername())){
                return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
            }     
            // Check if email exists in DB
            if(userRepository.existsByEmail(registerDTO.getEmail())){
                return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
            }

            User newUser =  userService.saveNewUser(registerDTO);

            if (newUser.equals(null))
                return CustomHttpResponse.generateRespose("Not able to save user.", HttpStatus.BAD_REQUEST, registerDTO);
            else
                return CustomHttpResponse.generateRespose("User saved successfully with id : " + newUser.getId(), HttpStatus.OK, newUser);

        }catch(Exception e){
            return CustomHttpResponse.generateRespose(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, "Could not register");
        }
    }  
}
