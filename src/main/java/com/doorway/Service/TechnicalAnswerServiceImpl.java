package com.doorway.Service;

import com.doorway.Exception.NotFoundException;
import com.doorway.Model.Interview;
import com.doorway.Model.TechnicalAnswer;
import com.doorway.Model.TechnicalQuestion;
import com.doorway.Payload.TechnicalAnswerPayload;
import com.doorway.Repository.TechnicalAnswerRepository;
import com.doorway.Service.Interface.InterviewService;
import com.doorway.Service.Interface.TechnicalAnswerService;
import com.doorway.Service.Interface.TechnicalQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TechnicalAnswerServiceImpl implements TechnicalAnswerService {

    private final TechnicalAnswerRepository technicalAnswerRepository;
    private final TechnicalQuestionService technicalQuestionService;
    private final InterviewService interviewService;

    @Autowired
    public TechnicalAnswerServiceImpl(TechnicalAnswerRepository technicalAnswerRepository,
                                      TechnicalQuestionService technicalQuestionService,
                                      InterviewService interviewService) {
        this.technicalAnswerRepository = technicalAnswerRepository;
        this.technicalQuestionService = technicalQuestionService;
        this.interviewService = interviewService;
    }

    @Override
    public TechnicalAnswer createTechnicalAnswer(TechnicalAnswerPayload technicalAnswerPayload, Long technicalQuestionId, UUID interviewId) {
        TechnicalQuestion technicalQuestion = technicalQuestionService.getTechnicalQuestion(technicalQuestionId);
        Interview interview = interviewService.getInterviewById(interviewId);

        technicalAnswerRepository.findByQuestionIdAndInterviewId(technicalQuestionId, interviewId)
                .ifPresent(existingAnswer -> {
                    throw new IllegalArgumentException("An answer for this question already exists in this interview.");
                });

        TechnicalAnswer technicalAnswer = technicalAnswerPayload.toEntity(technicalQuestion, interview);
        return technicalAnswerRepository.save(technicalAnswer);
    }


    @Override
    public TechnicalAnswer updateTechnicalAnswer(Long technicalQuestionId, TechnicalAnswerPayload technicalAnswerPayload, Long technicalAnswerId, UUID interviewId) {
        TechnicalAnswer existingAnswer = getTechnicalAnswerById(technicalAnswerId);
        TechnicalQuestion technicalQuestion = technicalQuestionService.getTechnicalQuestion(technicalQuestionId);
        Interview interview = interviewService.getInterviewById(interviewId);
        return technicalAnswerRepository.save(technicalAnswerPayload.toEntity(existingAnswer));
    }

    @Override
    public void deleteTechnicalAnswer(Long technicalAnswerId) {
        getTechnicalAnswerById(technicalAnswerId);
        technicalAnswerRepository.deleteById(technicalAnswerId);
    }

    @Override
    public TechnicalAnswer getTechnicalAnswerById(Long technicalAnswerId) {
        return technicalAnswerRepository.findById(technicalAnswerId)
                .orElseThrow(() -> new NotFoundException("Technical Answer not found"));
    }

    @Override
    public List<TechnicalAnswer> getTechnicalAnswersByInterviewId(UUID interviewId) {
        Interview interview = interviewService.getInterviewById(interviewId);
        if (interview == null) {
            throw new NotFoundException("Interview not found");
        }
        return technicalAnswerRepository.findByInterviewId(interviewId);
    }


}
