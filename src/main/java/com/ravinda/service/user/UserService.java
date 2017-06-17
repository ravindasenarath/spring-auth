package com.ravinda.service.user;

import com.ravinda.domain.User;
import com.ravinda.domain.VerificationToken;
import com.ravinda.dto.UserDto;

import java.io.UnsupportedEncodingException;

public interface UserService {

    User registerNewUserAccount(UserDto userDto);

    void createVerificationTokenForUser(User user, String token);

    VerificationToken generateNewTokenForUser(final String token);

    String validateVerificationToken(String token);

    User getUser(final String verificationToken);

    String generateQRUrl(User user) throws UnsupportedEncodingException;
}
