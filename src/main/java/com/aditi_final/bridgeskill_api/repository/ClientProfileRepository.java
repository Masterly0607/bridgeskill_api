package com.aditi_final.bridgeskill_api.repository;

import com.aditi_final.bridgeskill_api.entity.ClientProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientProfileRepository extends JpaRepository<ClientProfile, Long> {
    Optional<ClientProfile> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}