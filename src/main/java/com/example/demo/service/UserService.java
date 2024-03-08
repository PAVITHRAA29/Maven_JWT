package com.example.demo.service;

import com.example.demo.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public void saveUser(User user);

    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    User findByConfirmationToken(String confirmationToken);
    public List<Object> isUserPresent(User user);
    User findByResetToken(String resetToken);
    Optional<User> findByEmail(String email);
    void setResetToken(User user);
//    public UserDetails loadUserByUsername(String email);

}
