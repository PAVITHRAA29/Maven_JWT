package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class AuthController {
    @Autowired
    UserService userService;

    @Autowired
    private JavaMailSender javaMailSender;

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public String login(){
        return "auth/login";
    }

    @RequestMapping(value = {"/register"}, method = RequestMethod.GET)
    public String register(Model model){
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @RequestMapping(value = {"/register"}, method = RequestMethod.POST)
    public String registerUser(Model model, @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("successMessage", "Please fill all fields");
            model.addAttribute("bindingResult", bindingResult);
            return "auth/register";
        }
        List<Object> userPresentObj = userService.isUserPresent(user);
        if ((Boolean) userPresentObj.get(0)) {
            model.addAttribute("successMessage", userPresentObj.get(1));
            return "auth/register";
        }

        String confirmationToken = RandomStringUtils.randomAlphanumeric(30);
        user.setConfirmationToken(confirmationToken);
        //sendConfirmationEmail(user);
        userService.saveUser(user);
//
        model.addAttribute("successMessage", "A confirmation email has been sent. Please check your email to complete the registration.");

        return "auth/login";
    }

//    @RequestMapping(value = {"/forgot-password"}, method = RequestMethod.POST)
//    public String forgotPassword(Model model, @RequestParam("email") String email) {
//        System.out.println("Forgot Password method is called.");
//        Optional<User> userOptional = userService.findByEmail(email);
//
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//            userService.setResetToken(user);
//            sendPasswordResetEmail(user);
//            model.addAttribute("successMessage", "Password reset link sent. Check your email.");
//            return "auth/login";
//        } else {
//            model.addAttribute("errorMessage", "No user found with this email address.");
//            return "auth/login";
//        }
//    }
//
//
//    @RequestMapping(value = {"/reset-password"}, method = RequestMethod.POST)
//    public String resetPassword(Model model, @RequestParam("token") String token, @RequestParam("password") String password) {
//        User user = userService.findByResetToken(token);
//
//        if (user == null) {
//            model.addAttribute("errorMessage", "Invalid or expired reset token.");
//            return "auth/login";
//        }
//        user.setPassword(password);
//        user.setResetToken(null);
//        userService.saveUser(user);
//
//        model.addAttribute("successMessage", "Password reset successful. You can now log in.");
//        return "auth/login";
//    }
//
//    private void sendPasswordResetEmail(User user) {
//        String resetLink = "http://localhost:8080/reset-password?token=" + user.getResetToken();
//
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setTo(user.getEmail());
//        mailMessage.setFrom("your-email@gmail.com");
//        mailMessage.setSubject("Password Reset");
//        mailMessage.setText("Dear " + user.getUsername() + ",\n\nClick the link below to reset your password:\n\n" + resetLink);
//
//        javaMailSender.send(mailMessage);
//    }
//
//
//    @RequestMapping(value = {"/confirm"}, method = RequestMethod.GET)
//    public String confirmRegistration(Model model, @RequestParam("token") String token) {
//        System.out.println("Received confirmation token: " + token);
//
//        User user = userService.findByConfirmationToken(token);
//        if (user == null) {
//            System.out.println("Invalid confirmation token: " + token);
//            model.addAttribute("errorMessage", "Invalid confirmation token.");
//            return "auth/login";
//        }
//        // Confirming the user
//        user.setEnabled(true);
//        userService.saveUser(user);
//        System.out.println("User details saved successfully!");
//        model.addAttribute("successMessage", "User registered successfully! You can now log in.");
//        return "auth/login";
//    }
//
//    private void sendConfirmationEmail(User user) {
//        String confirmationLink = "http://localhost:8080/confirm?token=" + user.getConfirmationToken();
//
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setTo(user.getEmail());
//        mailMessage.setFrom("sivaranjani102515@gmail.com");
//        mailMessage.setSubject("Registration Confirmation");
//        mailMessage.setText("Dear " + user.getUsername() + ",\n\nThank you for registering! Please click the link below to confirm your registration:\n\n" + confirmationLink);
//
//        javaMailSender.send(mailMessage);
//    }

}
