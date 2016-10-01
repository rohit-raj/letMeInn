package com.rohit.letMeInn.service;

/**
 * Created by rohit on 2/10/16.
 */

import com.rohit.letMeInn.model.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
}
