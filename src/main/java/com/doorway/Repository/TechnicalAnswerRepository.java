package com.doorway.Repository;

import com.doorway.Model.TechnicalAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TechnicalAnswerRepository extends JpaRepository<TechnicalAnswer, Long> {
    Optional<TechnicalAnswer> findByQuestionIdAndInterviewId(Long questionId, UUID interviewId);
    List<TechnicalAnswer> findByInterviewId(UUID interviewId);
}
