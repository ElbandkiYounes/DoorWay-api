package com.doorway.Repository;

import com.doorway.Model.Decision;
import com.doorway.Model.Interviewee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface IntervieweeRepository extends JpaRepository<Interviewee, UUID> {
    List<Interviewee> findAllByInterviewingProcesses_Decision(Decision decision);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);

}
