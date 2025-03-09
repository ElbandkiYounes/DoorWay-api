package com.doorway.Service.Interface;

import com.doorway.Model.InterviewingProcess;
import com.doorway.Payload.InterviewingProcessPayload;

import java.util.List;
import java.util.UUID;

public interface InterviewingProcessService {
    InterviewingProcess getInterviewingProcessById(UUID interviewingProcessId);
    InterviewingProcess createInterviewingProcess(InterviewingProcessPayload interviewingProcessPayload);
    InterviewingProcess updateInterviewingProcess(UUID interviewingProcessId,InterviewingProcessPayload interviewingProcessPayload);
    void deleteInterviewingProcess(UUID interviewingProcessId);
    List<InterviewingProcess> getAllInterviewingProcessesByIntervieweeId(UUID intervieweeId);

}
