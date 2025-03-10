package com.doorway.Controller;

import com.doorway.Model.Interview;
import com.doorway.Payload.InterviewPayload;
import com.doorway.Service.Interface.InterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class InterviewController {

    private final InterviewService interviewService;

    @Autowired
    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @PostMapping("/interviewee/{intervieweeId}/interviewingprocess/{processId}/interviews")
    public ResponseEntity<Interview> createInterview(
            @PathVariable UUID intervieweeId,
            @PathVariable UUID processId,
            @RequestBody InterviewPayload interviewPayload) {
            Interview createdInterview = interviewService.createInterview(intervieweeId, processId, interviewPayload);
            return new ResponseEntity<>(createdInterview, HttpStatus.CREATED);
    }

    // Get an interview by its ID
    @GetMapping("/interviews/{interviewId}")
    public ResponseEntity<Interview> getInterviewById(@PathVariable UUID interviewId) {
            Interview interview = interviewService.getInterviewById(interviewId);
            return new ResponseEntity<>(interview, HttpStatus.OK);
    }

    // Get all interviews for a specific interviewee and process
    @GetMapping("/interviewee/{intervieweeId}/interviewingprocess/{processId}/interviews")
    public ResponseEntity<List<Interview>> getInterviewsByIntervieweeAndProcess(
            @PathVariable UUID intervieweeId,
            @PathVariable UUID processId) {
        List<Interview> interviews = interviewService.getInterviewsByProcess(intervieweeId, processId);
        return new ResponseEntity<>(interviews, HttpStatus.OK);
    }

    // Get all interviews for a specific interviewer
    @GetMapping("/interviewers/{interviewerId}/interviews")
    public ResponseEntity<List<Interview>> getInterviewsByInterviewer(@PathVariable UUID interviewerId) {
        List<Interview> interviews = interviewService.getInterviewsByInterviewer(interviewerId);
        return new ResponseEntity<>(interviews, HttpStatus.OK);
    }

    // Update an existing interview
    @PutMapping("/interviewee/{intervieweeId}/interviewingprocess/{interviewingProcessId}/interviews/{id}")
    public ResponseEntity<Interview> updateInterview(
            @PathVariable UUID intervieweeId,
            @PathVariable UUID interviewingProcessId,
            @PathVariable UUID id,
            @RequestBody InterviewPayload interviewPayload) {

        Interview updatedInterview = interviewService.updateInterview(id, intervieweeId, interviewingProcessId, interviewPayload);

        return new ResponseEntity<>(updatedInterview, HttpStatus.OK);
    }


    // Delete an interview by its ID
    @DeleteMapping("/interviews/{interviewId}")
    public ResponseEntity<Void> deleteInterview(@PathVariable UUID interviewId) {
            interviewService.deleteInterview(interviewId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
