package com.doorway.Repository;

import com.doorway.Model.Interviewer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InterviewerRepository extends JpaRepository<Interviewer, UUID> {
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<Interviewer> findByEmail(String email);
}
