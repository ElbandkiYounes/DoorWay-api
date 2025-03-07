package com.doorway.Repository;

import com.doorway.Model.TechnicalQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechnicalQuestionRepository extends JpaRepository<TechnicalQuestion, Long> {
}
