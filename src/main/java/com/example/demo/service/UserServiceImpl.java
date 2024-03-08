package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.RandomStringUtils;

import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserRepository userRepository;

    @Override
    public void saveUser(User user) {
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        String resetToken = RandomStringUtils.randomAlphanumeric(30);
        user.setResetToken(resetToken);
        userRepository.save(user);
    }
    @Override
    public List<Object> isUserPresent(User user) {
        boolean userExists = false;
        String message = null;
        Optional<User> existingUserEmail = userRepository.findByEmail(user.getEmail());
        if(existingUserEmail.isPresent()){
            userExists = true;
            message = "Email Already Present!";
        }
        Optional<User> existingUserMobile = userRepository.findByMobile(user.getMobile());
        if(existingUserMobile.isPresent()){
            userExists = true;
            message = "Mobile Number Already Present!";
        }
        if (existingUserEmail.isPresent() && existingUserMobile.isPresent()) {
            message = "Email and Mobile Number Both Already Present!";
        }
        System.out.println("existingUserEmail.isPresent() - "+existingUserEmail.isPresent()+"existingUserMobile.isPresent() - "+existingUserMobile.isPresent());
        return Arrays.asList(userExists, message);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(
                ()-> new UsernameNotFoundException(
                        String.format("USER_NOT_FOUND", email)
                ));
    }


    @Override
    public User findByConfirmationToken(String confirmationToken) {
        return userRepository.findByConfirmationToken(confirmationToken).orElse(null);
    }

    @Override
    public User findByResetToken(String resetToken) {
        return userRepository.findByResetToken(resetToken).orElse(null);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    @Override
    public void setResetToken(User user) {
        String resetToken = generateResetToken();
        user.setResetToken(resetToken);
        userRepository.save(user);
    }

    private String generateResetToken() {
        return RandomStringUtils.randomAlphanumeric(30);
    }

//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        // Implement OAuth2 user loading logic
//        // This method should return an implementation of OAuth2User
//        return buildOAuth2User(userRequest);
//    }
//
//    private OAuth2User buildOAuth2User(OAuth2UserRequest userRequest) {
//        // Extract user attributes from userRequest
//        // Customize this part based on the attributes your OAuth provider returns
//        String email = "user@example.com";
//        String name = "John Doe";
//
//        OAuth2UserAuthority authority = new OAuth2UserAuthority("ROLE_USER", userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri());
//        OAuth2UserAuthority adminAuthority = new OAuth2UserAuthority("ROLE_ADMIN", userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri());
//
//        OAuth2User user = new DefaultOAuth2User(
//                OAuth2UserAuthorityUtils.createAuthorities(authority, adminAuthority),
//                userRequest.getAdditionalParameters(),
//                "email"
//        );
//
//        return user;
//    }

}
