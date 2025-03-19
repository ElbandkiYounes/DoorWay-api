package com.doorway.Service.Interface;

import com.doorway.Model.Interviewer;
import com.doorway.Payload.InterviewerPayload;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface InterviewerService {

    Interviewer createInterviewer(InterviewerPayload payload, MultipartFile image);

    Interviewer getInterviewerById(UUID id);

    List<Interviewer> getAllInterviewers();

    Interviewer updateInterviewer(UUID id, InterviewerPayload payload, MultipartFile image);

    void deleteInterviewer(UUID id);

    Boolean getInterviewerByEmail(String email, UUID excludeId);

    Boolean getInterviewerByPhone(String phoneNumber, UUID excludeId);
}