package com.doorway.Controller;

import com.doorway.Model.Decision;
import com.doorway.Model.Interviewee;
import com.doorway.Payload.IntervieweePayload;
import com.doorway.Service.Interface.IntervieweeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/interviewees")
@Validated
public class IntervieweeController {

    private final IntervieweeService intervieweeService;

    public IntervieweeController(IntervieweeService intervieweeService) {
        this.intervieweeService = intervieweeService;
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Boolean> getIntervieweeByEmail(@PathVariable String email, @RequestParam(required = false) UUID excludeId) {
        Boolean exists = intervieweeService.getIntervieweeByEmail(email, excludeId);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/phoneNumber/{phoneNumber}")
    public ResponseEntity<Boolean> getIntervieweeByPhone(@PathVariable String phoneNumber, @RequestParam(required = false) UUID excludeId) {
        Boolean exists = intervieweeService.getIntervieweeByPhone(phoneNumber, excludeId);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Interviewee> getIntervieweeById(@PathVariable UUID id) {
        Interviewee interviewee = intervieweeService.getIntervieweeById(id);
        return ResponseEntity.ok(interviewee);
    }

    @GetMapping
    public ResponseEntity<List<Interviewee>> getAllInterviewees(@RequestParam(value = "decision", required = false) Decision decision) {
        List<Interviewee> interviewees = intervieweeService.getAllInterviewees(decision);
        return ResponseEntity.ok(interviewees);
    }

    @PostMapping
    public ResponseEntity<Interviewee> saveInterviewee(@Valid @RequestPart("payload") IntervieweePayload payload,
                                                       @RequestPart("image") MultipartFile image,
                                                       @RequestPart("resume") MultipartFile resume) {
        Interviewee interviewee = intervieweeService.saveInterviewee(payload, image, resume);
        return ResponseEntity.ok(interviewee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Interviewee> updateInterviewee(@PathVariable UUID id,
                                                         @Valid @RequestPart("payload") IntervieweePayload payload,
                                                         @RequestPart("image") MultipartFile image,
                                                         @RequestPart("resume") MultipartFile resume) {
        Interviewee interviewee = intervieweeService.updateInterviewee(id, payload, image, resume);
        return ResponseEntity.ok(interviewee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInterviewee(@PathVariable UUID id) {
        intervieweeService.deleteInterviewee(id);
        return ResponseEntity.noContent().build();
    }
}
