package com.doorway.Repository;

import com.doorway.Model.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface InterviewRepository extends JpaRepository<Interview, UUID> {
    List<Interview> findAllByInterviewerId(UUID interviewerId);
    List<Interview> findAllByInterviewingProcessId(UUID interviewingProcessId);

}
