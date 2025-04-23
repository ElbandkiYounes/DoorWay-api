package com.doorway.Controller;

import com.doorway.Model.Interviewee;
import com.doorway.Payload.IntervieweePayload;
import com.doorway.Service.Interface.IntervieweeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/public/applications")
@Validated
public class PublicApplicationController {

    private final IntervieweeService intervieweeService;

    public PublicApplicationController(IntervieweeService intervieweeService) {
        this.intervieweeService = intervieweeService;
    }

    @PostMapping
    public ResponseEntity<Interviewee> submitApplication(
            @Valid @RequestPart("payload") IntervieweePayload payload,
            @RequestPart("image") MultipartFile image,
            @RequestPart("resume") MultipartFile resume) {
        
        Interviewee interviewee = intervieweeService.saveInterviewee(payload, image, resume);
        return ResponseEntity.ok(interviewee);
    }
}