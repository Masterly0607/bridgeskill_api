package com.aditi_final.bridgeskill_api.repository;

import com.aditi_final.bridgeskill_api.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    boolean existsByJobIdAndStudentId(Long jobId, Long studentId);

    boolean existsByJobId(Long jobId);

    List<Application> findByStudentIdOrderByCreatedAtDesc(Long studentId);

    Optional<Application> findByIdAndStudentId(Long id, Long studentId);

    List<Application> findByJobIdOrderByCreatedAtDesc(Long jobId);

    long countByStatus(String status);

    List<Application> findTop5ByOrderByCreatedAtDesc();
}