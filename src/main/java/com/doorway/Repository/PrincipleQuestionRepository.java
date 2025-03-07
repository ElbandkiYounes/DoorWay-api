package com.doorway.Repository;

import com.doorway.Model.ExcellencePrinciple;
import com.doorway.Model.PrincipleQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrincipleQuestionRepository extends JpaRepository<PrincipleQuestion, Long> {
    List<PrincipleQuestion> findByPrinciple(ExcellencePrinciple principle);
}
