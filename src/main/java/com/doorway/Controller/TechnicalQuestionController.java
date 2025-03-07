package com.doorway.Controller;

import com.doorway.Payload.TechnicalQuestionPayload;
import com.doorway.Service.Interface.TechnicalQuestionService;
import com.doorway.Model.TechnicalQuestion;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/technical-questions")
public class TechnicalQuestionController {

    private final TechnicalQuestionService technicalQuestionService;

    public TechnicalQuestionController(TechnicalQuestionService technicalQuestionService) {
        this.technicalQuestionService = technicalQuestionService;
    }

    @PostMapping
    public ResponseEntity<TechnicalQuestion> addTechnicalQuestion(@Valid @RequestBody TechnicalQuestionPayload technicalQuestionPayload) {
        TechnicalQuestion technicalQuestion = technicalQuestionService.addTechnicalQuestion(technicalQuestionPayload);
        return new ResponseEntity<>(technicalQuestion, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TechnicalQuestion> getTechnicalQuestion(@PathVariable Long id) {
        TechnicalQuestion technicalQuestion = technicalQuestionService.getTechnicalQuestion(id);
        return new ResponseEntity<>(technicalQuestion, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TechnicalQuestion>> getAllTechnicalQuestions() {
        List<TechnicalQuestion> technicalQuestions = technicalQuestionService.getAllTechnicalQuestions();
        return new ResponseEntity<>(technicalQuestions, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TechnicalQuestion> updateTechnicalQuestion(@PathVariable Long id, @Valid @RequestBody TechnicalQuestionPayload technicalQuestionPayload) {
        TechnicalQuestion technicalQuestion = technicalQuestionService.updateTechnicalQuestion(id, technicalQuestionPayload);
        return new ResponseEntity<>(technicalQuestion, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechnicalQuestion(@PathVariable Long id) {
        technicalQuestionService.deleteTechnicalQuestion(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}