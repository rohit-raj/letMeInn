package com.rohit.letMeInn.repository;

/**
 * Created by rohit on 2/10/16.
 */

import com.rohit.letMeInn.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
