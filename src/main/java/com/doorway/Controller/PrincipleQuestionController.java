package com.doorway.Controller;

import com.doorway.Model.ExcellencePrinciple;
import com.doorway.Model.PrincipleQuestion;
import com.doorway.Payload.PrincipleQuestionPayload;
import com.doorway.Service.Interface.PrincipleQuestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/principle-questions")
public class PrincipleQuestionController {

    @Autowired
    private PrincipleQuestionService principleQuestionService;

    @GetMapping
    public ResponseEntity<List<PrincipleQuestion>> getPrincipleQuestions( @RequestParam(value = "principle", required = false) ExcellencePrinciple principle) {
        List<PrincipleQuestion> principleQuestions;
        if (principle != null) {
            principleQuestions = principleQuestionService.getPrincipleQuestionsByPrinciple(principle);
        } else {
            principleQuestions = principleQuestionService.getPrincipleQuestions();
        }
        return ResponseEntity.ok(principleQuestions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrincipleQuestion> getPrincipleQuestion(@PathVariable Long id) {
        PrincipleQuestion principleQuestion = principleQuestionService.getPrincipleQuestion(id);
        return ResponseEntity.ok(principleQuestion);
    }

    @PostMapping
    public ResponseEntity<PrincipleQuestion> addPrincipleQuestion(@Valid @RequestBody PrincipleQuestionPayload principleQuestionPayload) {
        PrincipleQuestion principleQuestion = principleQuestionService.addPrincipleQuestion(principleQuestionPayload);
        return ResponseEntity.ok(principleQuestion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrincipleQuestion> updatePrincipleQuestion(@PathVariable Long id, @Valid @RequestBody PrincipleQuestionPayload principleQuestionPayload) {
        PrincipleQuestion updatedPrincipleQuestion = principleQuestionService.updatePrincipleQuestion(id, principleQuestionPayload);
        return ResponseEntity.ok(updatedPrincipleQuestion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrincipleQuestion(@PathVariable Long id) {
        principleQuestionService.deletePrincipleQuestion(id);
        return ResponseEntity.noContent().build();
    }
}