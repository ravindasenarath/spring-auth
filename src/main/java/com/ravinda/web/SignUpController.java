package com.ravinda.web;

import com.ravinda.domain.User;
import com.ravinda.dto.UserDto;
import com.ravinda.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class SignUpController {

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/user/registration", method = RequestMethod.GET)
    public String showRegistrationForm(WebRequest request, Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "registration";
    }

    @RequestMapping(value = "/user/registration", method = RequestMethod.POST)
    public ModelAndView registerUserAccount(@ModelAttribute("user") @Valid final UserDto userDto, final HttpServletRequest request, final Errors errors){
        User registered = new User();
        if (!errors.hasErrors()) {
            registered = createUserAccount(userDto);
        }
        if (registered == null) {
            errors.rejectValue("email", "message.regError");
        }
        if (errors.hasErrors()) {
            return new ModelAndView("registration", "user", userDto);
        }
        else {
            return new ModelAndView("successRegister", "user", userDto);
        }
    }

    private User createUserAccount(UserDto userDto) {
        User registered = null;
        try {
            registered = userService.registerNewUserAccount(userDto);
        } catch (EmailExistsException e) {
            return null;
        }
        return registered;
    }
}
