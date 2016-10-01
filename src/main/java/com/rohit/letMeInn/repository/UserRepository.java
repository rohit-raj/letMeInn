package com.rohit.letMeInn.repository;

/**
 * Created by rohit on 2/10/16.
 */

import com.rohit.letMeInn.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);


}
