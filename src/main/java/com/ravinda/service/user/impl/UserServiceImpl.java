package com.ravinda.service.user.impl;

import com.ravinda.domain.User;
import com.ravinda.dto.UserDto;
import com.ravinda.repository.user.RoleRepository;
import com.ravinda.repository.user.UserRepository;
import com.ravinda.service.user.UserService;
import com.ravinda.web.EmailExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public User registerNewUserAccount(UserDto userDto) {
        if (emailExist(userDto.getEmail())) {
            throw new EmailExistsException("There is an account with that email adress: " + userDto.getEmail());
        }
        final User newUser = new User();

        newUser.setFirstName(userDto.getFirstName());
        newUser.setLastName(userDto.getLastName());
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        newUser.setEmail(userDto.getEmail());
        newUser.setUsing2FA(userDto.isUsing2FA());
        newUser.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
        return repository.save(newUser);
    }

    private boolean emailExist(final String email) {
        return repository.findByEmail(email) != null;
    }
}
