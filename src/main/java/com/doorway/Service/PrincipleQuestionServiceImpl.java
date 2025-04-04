package com.doorway.Service;

import com.doorway.Exception.ConflictException;
import com.doorway.Exception.NotFoundException;
import com.doorway.Model.ExcellencePrinciple;
import com.doorway.Model.PrincipleQuestion;
import com.doorway.Payload.PrincipleQuestionPayload;
import com.doorway.Repository.PrincipleQuestionRepository;
import com.doorway.Service.Interface.PrincipleQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrincipleQuestionServiceImpl implements PrincipleQuestionService {

    @Autowired
    private PrincipleQuestionRepository principleQuestionRepository;

    @Override
    public List<PrincipleQuestion> getPrincipleQuestionsByPrinciple(ExcellencePrinciple principle) {
        return principleQuestionRepository.findByPrinciple(principle);
    }

    @Override
    public List<PrincipleQuestion> getPrincipleQuestions() {
        return principleQuestionRepository.findAll();
    }

    @Override
    public PrincipleQuestion getPrincipleQuestion(Long id) {
        Optional<PrincipleQuestion> principleQuestion = principleQuestionRepository.findById(id);
        return principleQuestion.orElseThrow(() -> new NotFoundException("PrincipleQuestion not found with id: " + id));

    }

    @Override
    public PrincipleQuestion addPrincipleQuestion(PrincipleQuestionPayload principleQuestionPayload) {
        // Check if the question already exists
        PrincipleQuestion existingQuestion = principleQuestionRepository.findByQuestion(principleQuestionPayload.getQuestion());
        if (existingQuestion != null) {
            throw new ConflictException("PrincipleQuestion already exists" );
        }
        PrincipleQuestion principleQuestion = principleQuestionPayload.toEntity();
        return principleQuestionRepository.save(principleQuestion);
    }

    @Override
    public PrincipleQuestion updatePrincipleQuestion(Long id, PrincipleQuestionPayload principleQuestionPayload) {
        Optional<PrincipleQuestion> existingQuestion = principleQuestionRepository.findById(id);
        if (existingQuestion.isPresent()) {
            PrincipleQuestion updatedQuestion = principleQuestionPayload.toEntity(existingQuestion.get());
            return principleQuestionRepository.save(updatedQuestion);
        } else {
            throw new NotFoundException("PrincipleQuestion not found with id: " + id);
        }
    }

    @Override
    public void deletePrincipleQuestion(Long id) {
        Optional<PrincipleQuestion> principleQuestion = principleQuestionRepository.findById(id);
        if (principleQuestion.isPresent()) {
            principleQuestionRepository.delete(principleQuestion.get());
        } else {
            throw new NotFoundException("PrincipleQuestion not found with id: " + id);
        }
    }
}