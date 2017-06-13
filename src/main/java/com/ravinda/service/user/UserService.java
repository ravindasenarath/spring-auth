package com.ravinda.service.user;

import com.ravinda.domain.User;
import com.ravinda.dto.UserDto;

public interface UserService {

    User registerNewUserAccount(UserDto userDto);
}
