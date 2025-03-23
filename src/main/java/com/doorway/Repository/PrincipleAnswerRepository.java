package com.doorway.Repository;

import com.doorway.Model.PrincipleAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PrincipleAnswerRepository extends JpaRepository<PrincipleAnswer, Long> {
    Optional<PrincipleAnswer> findByQuestionIdAndInterviewId(Long question_id, UUID interview_id);
    List<PrincipleAnswer> findByInterviewId(UUID interview_id);
}
