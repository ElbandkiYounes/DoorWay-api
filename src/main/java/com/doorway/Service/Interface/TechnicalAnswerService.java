package com.doorway.Service.Interface;

import com.doorway.Model.TechnicalAnswer;
import com.doorway.Payload.TechnicalAnswerPayload;

import java.util.List;
import java.util.UUID;

public interface    TechnicalAnswerService {
    TechnicalAnswer createTechnicalAnswer(TechnicalAnswerPayload technicalAnswerPayload, Long technicalQuestionId, UUID interviewId);
    TechnicalAnswer updateTechnicalAnswer(Long technicalQuestionAnswerId,TechnicalAnswerPayload technicalAnswerPayload, Long technicalAnswerId, UUID interviewId);
    void deleteTechnicalAnswer(Long technicalAnswerId);
    TechnicalAnswer getTechnicalAnswerById(Long technicalAnswerId);
    List<TechnicalAnswer> getTechnicalAnswersByInterviewId(UUID interviewId);

}
