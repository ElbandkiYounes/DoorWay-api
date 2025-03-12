package com.doorway.Controller;

import com.doorway.Model.PrincipleAnswer;
import com.doorway.Payload.PrincipleAnswerPayload;
import com.doorway.Service.Interface.PrincipleAnswerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/principle-answers")
@Validated
public class PrincipleAnswerController {

    private final PrincipleAnswerService principleAnswerService;

    public PrincipleAnswerController(PrincipleAnswerService principleAnswerService) {
        this.principleAnswerService = principleAnswerService;
    }

    // Create a new PrincipleAnswer
    @PostMapping("/{principleQuestionId}/interview/{interviewId}")
    public ResponseEntity<PrincipleAnswer> createPrincipleAnswer(
            @PathVariable Long principleQuestionId,
            @PathVariable UUID interviewId,
            @Valid @RequestBody PrincipleAnswerPayload principleAnswerPayload) {

        PrincipleAnswer createdAnswer = principleAnswerService.createPrincipleAnswer(principleAnswerPayload, interviewId, principleQuestionId);
        return ResponseEntity.ok(createdAnswer);
    }

    // Update an existing PrincipleAnswer
    @PutMapping("/{principleAnswerId}/question/{principleQuestionId}/interview/{interviewId}")
    public ResponseEntity<PrincipleAnswer> updatePrincipleAnswer(
            @PathVariable Long principleAnswerId,
            @PathVariable Long principleQuestionId,
            @PathVariable UUID interviewId,
            @Valid @RequestBody PrincipleAnswerPayload principleAnswerPayload) {

        PrincipleAnswer updatedAnswer = principleAnswerService.updatePrincipleAnswer(principleAnswerId, principleAnswerPayload, interviewId, principleQuestionId);
        return ResponseEntity.ok(updatedAnswer);
    }

    // Delete a PrincipleAnswer by ID
    @DeleteMapping("/{principleAnswerId}")
    public ResponseEntity<Void> deletePrincipleAnswer(@PathVariable Long principleAnswerId) {
        principleAnswerService.deletePrincipleAnswer(principleAnswerId);
        return ResponseEntity.noContent().build();
    }

    // Get a PrincipleAnswer by ID
    @GetMapping("/{principleAnswerId}")
    public ResponseEntity<PrincipleAnswer> getPrincipleAnswerById(@PathVariable Long principleAnswerId) {
        PrincipleAnswer principleAnswer = principleAnswerService.getPrincipleAnswerById(principleAnswerId);
        return ResponseEntity.ok(principleAnswer);
    }
}
