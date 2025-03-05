package com.doorway.Repository;

import com.doorway.Model.Interviewer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InterviewerRepository extends JpaRepository<Interviewer, UUID> {

}
