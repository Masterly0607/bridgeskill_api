package com.aditi_final.bridgeskill_api.repository;

import com.aditi_final.bridgeskill_api.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByClientId(Long clientId);
    List<Job> findTop5ByOrderByCreatedAtDesc();
}