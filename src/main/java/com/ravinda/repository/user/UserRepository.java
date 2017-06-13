package com.ravinda.repository.user;

import com.ravinda.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User, Long> {

    User findByEmail(String email);

    @Override
    void delete(User user);

}
