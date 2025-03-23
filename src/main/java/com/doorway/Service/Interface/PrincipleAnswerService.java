package com.doorway.Service.Interface;

import com.doorway.Model.PrincipleAnswer;
import com.doorway.Payload.PrincipleAnswerPayload;

import java.util.List;
import java.util.UUID;

public interface PrincipleAnswerService {

    PrincipleAnswer createPrincipleAnswer(PrincipleAnswerPayload payload, UUID interviewId,Long principleQuestionId);

    PrincipleAnswer updatePrincipleAnswer(Long id, PrincipleAnswerPayload payload, UUID interviewId ,Long principleQuestionId);

    void deletePrincipleAnswer(Long id);

    PrincipleAnswer getPrincipleAnswerById(Long id);

    List<PrincipleAnswer> getPrincipleAnswersByInterviewId(UUID interviewId);
}
