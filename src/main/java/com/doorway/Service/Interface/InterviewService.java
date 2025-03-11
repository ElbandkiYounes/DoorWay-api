package com.doorway.Service.Interface;


import com.doorway.Model.Interview;
import com.doorway.Payload.InterviewPayload;

import java.util.List;
import java.util.UUID;

public interface InterviewService {


    Interview createInterview(UUID intervieweeId, UUID processId,  InterviewPayload payload);
    Interview updateInterview(UUID interviewId, UUID intervieweeId, UUID processId, InterviewPayload payload);
    Interview getInterviewById(UUID interviewId);

    List<Interview> getInterviewsByProcess(UUID intervieweeId, UUID processId);

    List<Interview> getInterviewsByInterviewer(UUID interviewerId);

    void deleteInterview(UUID interviewId);


}