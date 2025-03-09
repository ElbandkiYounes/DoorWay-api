package com.doorway.Repository;

import com.doorway.Model.InterviewingProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface InterviewingProcessRepository extends JpaRepository<InterviewingProcess, UUID> {
    List<InterviewingProcess> findAllByIntervieweeId(UUID intervieweeId);
}
