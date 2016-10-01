package com.rohit.letMeInn.service;

/**
 * Created by rohit on 2/10/16.
 */

public interface SecurityService {
    String findLoggedInUsername();

    void autologin(String username, String password);
}
