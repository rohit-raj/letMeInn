package com.rohit.letMeInn.web;

/**
 * Created by rohit on 2/10/16.
 */

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.rohit.letMeInn.model.Role;
import com.rohit.letMeInn.model.User;
import com.rohit.letMeInn.repository.RoleRepository;
import com.rohit.letMeInn.service.SecurityService;
import com.rohit.letMeInn.service.UserService;
import com.rohit.letMeInn.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("userForm", new User());

        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        userService.save(userForm);

        securityService.autologin(userForm.getUsername(), userForm.getPasswordConfirm());

        return "redirect:/welcome";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }

    @RequestMapping(value = {"/", "/welcome"}, method = RequestMethod.GET)
    public String welcome(Model model) {
        return "welcome";
    }

    @RequestMapping(value = {"/user"}, method = RequestMethod.GET, produces = "application/json")
    @ResponseBody()
    public Map<String, Object> details(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUser = auth.getName();
        String authority = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()[0].toString();
        User user = userService.findByUsername(loggedInUser);

        Map<String, Object> userDetails = new HashMap<>();
        if(user != null){
            userDetails.putIfAbsent("username", user.getUsername());
            userDetails.putIfAbsent("email", user.getEmail());
            userDetails.putIfAbsent("phone", user.getPhone());
            if(authority.equals("ROLE_USER")){
                userDetails.putIfAbsent("role", "USER");
            } else if(authority.equals("ROLE_ADMIN")){
                userDetails.putIfAbsent("role", "ADMIN");
            }
        } else {
            userDetails.putIfAbsent("Message", "You are signed in as anonymous user, please sign in to get the details");
        }

        return userDetails;
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public String update(Model model) {
        model.addAttribute("updateForm", new User());

        return "update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@ModelAttribute("updateForm") User updateForm, BindingResult bindingResult, Model model) {
        userValidator.updateValidation(updateForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "update";
        }

        userService.updateUser(updateForm);
        userService.findByUsername(updateForm.getUsername());

        return "redirect:/welcome";
    }
}
