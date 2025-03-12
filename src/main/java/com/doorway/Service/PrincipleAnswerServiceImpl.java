package com.doorway.Service;

import com.doorway.Exception.NotFoundException;
import com.doorway.Model.PrincipleAnswer;
import com.doorway.Model.PrincipleQuestion;
import com.doorway.Model.Interview;
import com.doorway.Payload.PrincipleAnswerPayload;
import com.doorway.Repository.PrincipleAnswerRepository;
import com.doorway.Service.Interface.PrincipleAnswerService;
import com.doorway.Service.Interface.PrincipleQuestionService;
import com.doorway.Service.Interface.InterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PrincipleAnswerServiceImpl implements PrincipleAnswerService {

    private final PrincipleAnswerRepository principleAnswerRepository;
    private final PrincipleQuestionService principleQuestionService;
    private final InterviewService interviewService;

    @Autowired
    public PrincipleAnswerServiceImpl(PrincipleAnswerRepository principleAnswerRepository,
                                      PrincipleQuestionService principleQuestionService,
                                      InterviewService interviewService) {
        this.principleAnswerRepository = principleAnswerRepository;
        this.principleQuestionService = principleQuestionService;
        this.interviewService = interviewService;
    }


    @Override
    public PrincipleAnswer createPrincipleAnswer(PrincipleAnswerPayload payload, UUID interviewId, Long principleQuestionId) {
        PrincipleQuestion principleQuestion = principleQuestionService.getPrincipleQuestion(principleQuestionId);
        Interview interview = interviewService.getInterviewById(interviewId);

        principleAnswerRepository.findByQuestionIdAndInterviewId(principleQuestionId, interviewId)
                .ifPresent(existingAnswer -> {
                    throw new IllegalArgumentException("An answer for this question already exists in this interview.");
                });

        PrincipleAnswer principleAnswer = payload.toEntity(principleQuestion, interview);
        return principleAnswerRepository.save(principleAnswer);
    }

    @Override
    public PrincipleAnswer updatePrincipleAnswer(Long id, PrincipleAnswerPayload payload, UUID interviewId, Long principleQuestionId) {
        PrincipleAnswer existingAnswer = getPrincipleAnswerById(id);
        PrincipleQuestion principleQuestion = principleQuestionService.getPrincipleQuestion(principleQuestionId);
        Interview interview = interviewService.getInterviewById(interviewId);

        return principleAnswerRepository.save(payload.toEntity(existingAnswer));
    }

    @Override
    public void deletePrincipleAnswer(Long id) {
        getPrincipleAnswerById(id);
        principleAnswerRepository.deleteById(id);
    }

    @Override
    public PrincipleAnswer getPrincipleAnswerById(Long id) {
        return principleAnswerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("PrincipleAnswer not found"));
    }
}
