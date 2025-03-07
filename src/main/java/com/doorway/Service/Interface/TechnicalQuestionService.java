package com.doorway.Service.Interface;

import com.doorway.Model.TechnicalQuestion;
import com.doorway.Payload.TechnicalQuestionPayload;

import java.util.List;

public interface TechnicalQuestionService {
    TechnicalQuestion addTechnicalQuestion(TechnicalQuestionPayload technicalQuestionPayload);
    TechnicalQuestion getTechnicalQuestion(Long id);
    List<TechnicalQuestion> getAllTechnicalQuestions();
    TechnicalQuestion updateTechnicalQuestion(Long id, TechnicalQuestionPayload technicalQuestionPayload);
    void deleteTechnicalQuestion(Long id);
}
