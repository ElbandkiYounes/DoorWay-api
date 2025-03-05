package com.doorway.Controller;

import com.doorway.Model.Interviewer;
import com.doorway.Payload.InterviewerPayload;
import com.doorway.Service.InterviewerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/interviewers")
public class InterviewerController {

    private final InterviewerService interviewerService;

    @Autowired
    public InterviewerController(InterviewerService interviewerService) {
        this.interviewerService = interviewerService;
    }

    @PostMapping
    public ResponseEntity<Interviewer> createInterviewer(@RequestBody InterviewerPayload payload) throws IOException {
        Interviewer interviewer = interviewerService.createInterviewer(payload);
        return ResponseEntity.ok(interviewer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Interviewer> getInterviewerById(@PathVariable UUID id) {
        Interviewer interviewer = interviewerService.getInterviewerById(id);
        return ResponseEntity.ok(interviewer);
    }

    @GetMapping()
    public ResponseEntity<List<Interviewer>> getAllInterviewers() {
        List<Interviewer> interviewers = interviewerService.getAllInterviewers();
        return ResponseEntity.ok(interviewers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Interviewer> updateInterviewer(@PathVariable UUID id, @RequestBody InterviewerPayload payload) throws IOException {
        Interviewer interviewer = interviewerService.updateInterviewer(id, payload);
        return ResponseEntity.ok(interviewer);
    }
}