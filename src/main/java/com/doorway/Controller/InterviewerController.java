package com.doorway.Controller;

import com.doorway.Model.Interviewer;
import com.doorway.Payload.InterviewerPayload;
import com.doorway.Service.Interface.InterviewerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/interviewers")
@Validated // Enable validation for this controller
public class InterviewerController {

    private final InterviewerService interviewerService;

    @Autowired
    public InterviewerController(InterviewerService interviewerService) {
        this.interviewerService = interviewerService;
    }

    @PostMapping
    public ResponseEntity<Interviewer> createInterviewer(
            @Valid @RequestPart("payload") InterviewerPayload payload, // Ensure @Valid is applied
            @RequestPart("image") MultipartFile image) {
        Interviewer interviewer = interviewerService.createInterviewer(payload, image);
        return ResponseEntity.ok(interviewer);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Boolean> getInterviewerByEmail(@PathVariable String email, @RequestParam(required = false) UUID excludeId) {
        Boolean exists = interviewerService.getInterviewerByEmail(email, excludeId);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/phoneNumber/{phoneNumber}")
    public ResponseEntity<Boolean> getInterviewerByPhone(@PathVariable String phoneNumber, @RequestParam(required = false) UUID excludeId) {
        Boolean exists = interviewerService.getInterviewerByPhone(phoneNumber, excludeId);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Interviewer> getInterviewerById(@PathVariable UUID id) {
        Interviewer interviewer = interviewerService.getInterviewerById(id);
        return ResponseEntity.ok(interviewer);
    }

    @GetMapping
    public ResponseEntity<List<Interviewer>> getAllInterviewers() {
        List<Interviewer> interviewers = interviewerService.getAllInterviewers();
        return ResponseEntity.ok(interviewers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Interviewer> updateInterviewer(
            @PathVariable UUID id,
            @Valid @RequestPart("payload") InterviewerPayload payload,
            @RequestPart("image") MultipartFile image) {
        Interviewer interviewer = interviewerService.updateInterviewer(id, payload, image);
        return ResponseEntity.ok(interviewer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInterviewer(@PathVariable UUID id) {
        interviewerService.deleteInterviewer(id);
        return ResponseEntity.noContent().build();
    }
}