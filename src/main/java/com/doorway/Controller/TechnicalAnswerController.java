package com.doorway.Controller;

import com.doorway.Model.TechnicalAnswer;
import com.doorway.Payload.TechnicalAnswerPayload;
import com.doorway.Service.Interface.TechnicalAnswerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/technical-answers")
@Validated
public class TechnicalAnswerController {

    private final TechnicalAnswerService technicalAnswerService;

    public TechnicalAnswerController(TechnicalAnswerService technicalAnswerService) {
        this.technicalAnswerService = technicalAnswerService;
    }

    @PostMapping("/{technicalQuestionId}/interview/{interviewId}")
    public ResponseEntity<TechnicalAnswer> createTechnicalAnswer(
            @PathVariable Long technicalQuestionId,
            @PathVariable UUID interviewId,
            @Valid @RequestBody TechnicalAnswerPayload technicalAnswerPayload) {

        TechnicalAnswer createdAnswer = technicalAnswerService.createTechnicalAnswer(technicalAnswerPayload, technicalQuestionId, interviewId);
        return ResponseEntity.ok(createdAnswer);
    }

    @PutMapping("/{technicalAnswerId}/question/{technicalQuestionId}/interview/{interviewId}")
    public ResponseEntity<TechnicalAnswer> updateTechnicalAnswer(
            @PathVariable Long technicalAnswerId,
            @PathVariable Long technicalQuestionId,
            @PathVariable UUID interviewId,
            @Valid @RequestBody TechnicalAnswerPayload technicalAnswerPayload) {

        TechnicalAnswer updatedAnswer = technicalAnswerService.updateTechnicalAnswer(technicalQuestionId, technicalAnswerPayload, technicalAnswerId, interviewId);
        return ResponseEntity.ok(updatedAnswer);
    }

    @DeleteMapping("/{technicalAnswerId}")
    public ResponseEntity<Void> deleteTechnicalAnswer(@PathVariable Long technicalAnswerId) {
        technicalAnswerService.deleteTechnicalAnswer(technicalAnswerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{technicalAnswerId}")
    public ResponseEntity<TechnicalAnswer> getTechnicalAnswerById(@PathVariable Long technicalAnswerId) {
        TechnicalAnswer technicalAnswer = technicalAnswerService.getTechnicalAnswerById(technicalAnswerId);
        return ResponseEntity.ok(technicalAnswer);
    }

    @GetMapping("/interview/{interviewId}")
    public ResponseEntity<List<TechnicalAnswer>> getTechnicalAnswersByInterviewId(@PathVariable UUID interviewId) {
        return ResponseEntity.ok(technicalAnswerService.getTechnicalAnswersByInterviewId(interviewId));
    }


}
