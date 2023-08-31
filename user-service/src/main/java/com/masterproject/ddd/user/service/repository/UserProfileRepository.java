package com.masterproject.ddd.user.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.masterproject.ddd.core.api.user.model.UserStatus;
import com.masterproject.ddd.user.service.model.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {

    List<UserProfile> findByStatus(UserStatus status);

    List<UserProfile> findByUsername(String username);

    List<UserProfile> findByEmail(String email);

}
