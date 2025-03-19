package com.doorway.Service.Interface;

import com.doorway.Model.Decision;
import com.doorway.Model.Interviewee;
import com.doorway.Payload.IntervieweePayload;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.UUID;

public interface IntervieweeService {
    Interviewee getIntervieweeById(UUID id);
    List<Interviewee> getAllInterviewees(Decision decision);
    Interviewee saveInterviewee(IntervieweePayload intervieweePayload, MultipartFile image, MultipartFile resume);
    Interviewee updateInterviewee(UUID id, IntervieweePayload intervieweePayload, MultipartFile image, MultipartFile resume);
    void deleteInterviewee(UUID id);

    Boolean getIntervieweeByPhone(String phone, UUID excludeId);

    Boolean getIntervieweeByEmail(String email, UUID excludeId);
}
