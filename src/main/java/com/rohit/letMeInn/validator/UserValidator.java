package com.rohit.letMeInn.validator;

/**
 * Created by rohit on 2/10/16.
 */

import com.rohit.letMeInn.model.User;
import com.rohit.letMeInn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class UserValidator implements Validator {
    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty");
        if (user.getUsername().length() < 6 || user.getUsername().length() > 32) {
            errors.rejectValue("username", "Size.userForm.username");
        }
        if (userService.findByUsername(user.getUsername()) != null) {
            errors.rejectValue("username", "Duplicate.userForm.username");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
        if (user.getPassword().length() < 8 || user.getPassword().length() > 32) {
            errors.rejectValue("password", "Size.userForm.password");
        }

        if (!user.getPasswordConfirm().equals(user.getPassword())) {
            errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm");
        }


    }

    public void updateValidation(Object o, Errors errors) {
        User user = (User) o;

        if(!StringUtils.isEmpty(user.getEmail())) {
            String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
            java.util.regex.Pattern emailPattern = java.util.regex.Pattern.compile(ePattern);
            java.util.regex.Matcher emailMatcher = emailPattern.matcher(user.getEmail());

            if (!emailMatcher.matches()) {
                errors.rejectValue("email", "Invalid.updateForm.email");
            }

        } else {
            errors.rejectValue("email", "Enter.updateForm.email");
        }

        if(!StringUtils.isEmpty(user.getPhone())) {
            String PHONE_REGEX = "^\\+([0-9\\-]?){9,11}[0-9]$";
            java.util.regex.Pattern phonePattern = java.util.regex.Pattern.compile(PHONE_REGEX);
            java.util.regex.Matcher phoneMatcher = phonePattern.matcher(user.getPhone());
            if (!phoneMatcher.matches() || user.getPhone().length() > 20) {
                errors.rejectValue("phone", "Invalid.updateForm.phone");
            }
        }  else {
            errors.rejectValue("phone", "Enter.updateForm.phone");
        }
    }


}
