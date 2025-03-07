package com.doorway.Service.Interface;

import com.doorway.Model.ExcellencePrinciple;
import com.doorway.Model.PrincipleQuestion;
import com.doorway.Payload.PrincipleQuestionPayload;

import java.util.List;

public interface PrincipleQuestionService {
    List<PrincipleQuestion> getPrincipleQuestionsByPrinciple(ExcellencePrinciple principle);
    List<PrincipleQuestion> getPrincipleQuestions();
    PrincipleQuestion getPrincipleQuestion(Long id);
    PrincipleQuestion addPrincipleQuestion(PrincipleQuestionPayload principleQuestionPayload);
    PrincipleQuestion updatePrincipleQuestion(Long id, PrincipleQuestionPayload principleQuestionPayload);
    void deletePrincipleQuestion(Long id);
}
