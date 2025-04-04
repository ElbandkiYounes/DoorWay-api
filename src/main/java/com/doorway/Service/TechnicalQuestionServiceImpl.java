package com.doorway.Service;

import com.doorway.Exception.ConflictException;
import com.doorway.Exception.NotFoundException;
import com.doorway.Model.TechnicalQuestion;
import com.doorway.Payload.TechnicalQuestionPayload;
import com.doorway.Repository.TechnicalQuestionRepository;
import com.doorway.Service.Interface.TechnicalQuestionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TechnicalQuestionServiceImpl implements TechnicalQuestionService {
    private final TechnicalQuestionRepository technicalQuestionRepository;

    public TechnicalQuestionServiceImpl(TechnicalQuestionRepository technicalQuestionRepository) {
        this.technicalQuestionRepository = technicalQuestionRepository;
    }

    public TechnicalQuestion addTechnicalQuestion(TechnicalQuestionPayload technicalQuestionPayload) {
        // Check if the question already exists
        TechnicalQuestion existingQuestion = technicalQuestionRepository.findByQuestion(technicalQuestionPayload.getQuestion());
        if (existingQuestion != null) {
            throw new ConflictException("Technical Question already exists");
        }
        return technicalQuestionRepository.save(technicalQuestionPayload.toEntity());
    }

    public TechnicalQuestion getTechnicalQuestion(Long id) {
        return technicalQuestionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Technical Question not found"));
    }

    public List<TechnicalQuestion> getAllTechnicalQuestions() {
        return technicalQuestionRepository.findAll();
    }
    public TechnicalQuestion updateTechnicalQuestion(Long id, TechnicalQuestionPayload technicalQuestionPayload) {
        TechnicalQuestion technicalQuestion = getTechnicalQuestion(id);
        return technicalQuestionRepository.save(technicalQuestionPayload.toEntity(technicalQuestion));
    }

    public void deleteTechnicalQuestion(Long id) {
        technicalQuestionRepository.delete(getTechnicalQuestion(id));
    }

}
