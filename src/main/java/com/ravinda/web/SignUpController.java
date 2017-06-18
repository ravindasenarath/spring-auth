package com.ravinda.web;

import com.ravinda.domain.User;
import com.ravinda.dto.UserDto;
import com.ravinda.event.OnRegistrationCompleteEvent;
import com.ravinda.service.user.UserService;
import com.ravinda.web.utils.GenericResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

@Controller
public class SignUpController {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messages;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @RequestMapping(value = "/user/registration", method = RequestMethod.GET)
    public String showRegistrationForm(WebRequest request, Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "registration";
    }

    @RequestMapping(value = "/user/registration", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<GenericResponse> registerUserAccount(@Valid final UserDto accountDto, final HttpServletRequest request) {
        LOGGER.debug("Registering user account with information: {}", accountDto);
        try {
            final User registered = userService.registerNewUserAccount(accountDto);
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), getAppUrl(request)));
            return new ResponseEntity<>(new GenericResponse("success"), HttpStatus.OK);
        } catch (EmailExistsException e){
            LOGGER.error("User already exist with given email", e);
            return new ResponseEntity<>(new GenericResponse("User already exist with given email", "UserAlreadyExist"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e){
            LOGGER.error("Error happened", e);
            return new ResponseEntity<>(new GenericResponse(e.getMessage(), "InternalError"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
    public String confirmRegistration(final Locale locale, final Model model, @RequestParam("token") final String token)  throws UnsupportedEncodingException {
        final String result = userService.validateVerificationToken(token);
        if (result.equals("valid")) {
            final User user = userService.getUser(token);
            //TODO logging
            System.out.println(user);
            if (user.isUsing2FA()) {
                model.addAttribute("qr", userService.generateQRUrl(user));
                return "redirect:/qrcode.html?lang=" + locale.getLanguage();
            }
            model.addAttribute("message", messages.getMessage("message.accountVerified", null, locale));
            return "redirect:/login?lang=" + locale.getLanguage();
        }

        model.addAttribute("message", messages.getMessage("auth.message." + result, null, locale));
        model.addAttribute("expired", "expired".equals(result));
        model.addAttribute("token", token);
        return "redirect:/badUser.html?lang=" + locale.getLanguage();
    }

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
