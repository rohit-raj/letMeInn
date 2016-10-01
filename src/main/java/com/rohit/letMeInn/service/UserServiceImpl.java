package com.rohit.letMeInn.service;

/**
 * Created by rohit on 2/10/16.
 */

import com.rohit.letMeInn.model.Role;
import com.rohit.letMeInn.model.User;
import com.rohit.letMeInn.repository.RoleRepository;
import com.rohit.letMeInn.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Long randomNumber = 1 + (long)(Math.random() * (roleRepository.findAll().size()));
        HashSet<Role> randomRoleSelected = new HashSet<>();
        randomRoleSelected.add(roleRepository.findOne(randomNumber));

        user.setRoles(randomRoleSelected);
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
