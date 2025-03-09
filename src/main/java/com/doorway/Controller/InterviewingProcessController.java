package com.doorway.Controller;

import com.doorway.Model.InterviewingProcess;
import com.doorway.Payload.InterviewingProcessPayload;
import com.doorway.Service.Interface.InterviewingProcessService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/interviewing-processes")
@Validated
public class InterviewingProcessController {
    private final InterviewingProcessService interviewingProcessService;

    @Autowired
    public InterviewingProcessController(InterviewingProcessService interviewingProcessService) {
        this.interviewingProcessService = interviewingProcessService;
    }

    @PostMapping
    public ResponseEntity<InterviewingProcess> createInterviewingProcess(@Valid @RequestBody InterviewingProcessPayload interviewingProcessPayload) {
        InterviewingProcess interviewingProcess = interviewingProcessService.createInterviewingProcess(interviewingProcessPayload);
        return ResponseEntity.status(HttpStatus.CREATED).body(interviewingProcess);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InterviewingProcess> getInterviewingProcessById(@PathVariable UUID id) {
        InterviewingProcess interviewingProcess = interviewingProcessService.getInterviewingProcessById(id);
        return ResponseEntity.ok(interviewingProcess);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InterviewingProcess> updateInterviewingProcess(@PathVariable UUID id, @Valid @RequestBody InterviewingProcessPayload interviewingProcessPayload) {
        InterviewingProcess interviewingProcess = interviewingProcessService.updateInterviewingProcess(id, interviewingProcessPayload);
        return ResponseEntity.ok(interviewingProcess);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInterviewingProcess(@PathVariable UUID id) {
        interviewingProcessService.deleteInterviewingProcess(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/interviewee/{intervieweeId}")
    public ResponseEntity<List<InterviewingProcess>> getAllInterviewingProcessesByIntervieweeId(@PathVariable UUID intervieweeId) {
        List<InterviewingProcess> interviewingProcesses = interviewingProcessService.getAllInterviewingProcessesByIntervieweeId(intervieweeId);
        return ResponseEntity.ok(interviewingProcesses);
    }
}