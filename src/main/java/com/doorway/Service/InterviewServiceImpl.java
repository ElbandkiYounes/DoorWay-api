package com.doorway.Service;

import com.doorway.Exception.ConflictException;
import com.doorway.Exception.NotFoundException;
import com.doorway.Model.Interview;
import com.doorway.Model.Interviewee;
import com.doorway.Model.Interviewer;
import com.doorway.Model.InterviewingProcess;
import com.doorway.Payload.InterviewPayload;
import com.doorway.Repository.InterviewRepository;
import com.doorway.Service.Interface.InterviewService;
import com.doorway.Service.Interface.IntervieweeService;
import com.doorway.Service.Interface.InterviewerService;
import com.doorway.Service.Interface.InterviewingProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
public class InterviewServiceImpl implements InterviewService {

    InterviewerService interviewerService;
    InterviewingProcessService interviewingProcessService;
    IntervieweeService intervieweeService;
    InterviewRepository interviewRepository;
    @Autowired
    public InterviewServiceImpl(InterviewerService interviewerService, InterviewingProcessService interviewingProcessService, IntervieweeService intervieweeService, InterviewRepository interviewRepository) {
        this.interviewerService = interviewerService;
        this.interviewingProcessService = interviewingProcessService;
        this.intervieweeService = intervieweeService;
        this.interviewRepository = interviewRepository;
    }


    @Override
    public Interview createInterview(UUID intervieweeId, UUID processId, InterviewPayload interviewPayload) {
        InterviewingProcess interviewingProcess = interviewingProcessService.getInterviewingProcessById(processId);
        if (interviewingProcess == null) {
            throw new NotFoundException("Interviewing Process not found");
        }

        Interviewee interviewee = intervieweeService.getIntervieweeById(intervieweeId);
        if (interviewee == null) {
            throw new NotFoundException("Interviewee not found");
        }

        if (!interviewingProcess.getInterviewee().equals(interviewee)) {
            throw new ConflictException("Interviewee does not belong to the provided interviewing process");
        }


        Interviewer interviewer = interviewerService.getInterviewerById(interviewPayload.getInterviewerId());

        if (interviewer == null) {
            throw new NotFoundException("Interviewer not found");
        }

            return interviewRepository.save(interviewPayload.toEntity(interviewer, interviewingProcess));



    }

    @Override
    public Interview getInterviewById(UUID interviewId) {
        return interviewRepository.findById(interviewId).orElseThrow(() -> new NotFoundException("Interview not found"));
    }

    @Override
    public List<Interview> getInterviewsByProcess(UUID intervieweeId, UUID processId) {
        InterviewingProcess interviewingProcess = interviewingProcessService.getInterviewingProcessById(processId);
        if (interviewingProcess == null) {
            throw new NotFoundException("Interviewing Process not found");
        }
        Interviewee interviewee = intervieweeService.getIntervieweeById(intervieweeId);
        if (interviewee == null) {
            throw new NotFoundException("Interviewee not found");
        }
        return interviewRepository.findAllByInterviewingProcessId(processId);
    }

    @Override
    public List<Interview> getInterviewsByInterviewer(UUID interviewerId) {
        Interviewer interviewer = interviewerService.getInterviewerById(interviewerId);
        if (interviewer == null) {
            throw new NotFoundException("Interviewer not found");
        }
        return interviewRepository.findAllByInterviewerId(interviewerId);
    }

    @Override
    public void deleteInterview(UUID interviewId) {
        getInterviewById(interviewId);
        interviewRepository.deleteById(interviewId);
    }

    @Override
    public Interview updateInterview(UUID interviewId, UUID intervieweeId, UUID processId, InterviewPayload interviewPayload) {
        InterviewingProcess interviewingProcess = interviewingProcessService.getInterviewingProcessById(processId);
        if (interviewingProcess == null) {
            throw new NotFoundException("Interviewing Process not found");
        }

        Interviewee interviewee = intervieweeService.getIntervieweeById(intervieweeId);
        if (interviewee == null) {
            throw new NotFoundException("Interviewee not found");
        }

        if (!interviewingProcess.getInterviewee().getId().equals(interviewee.getId())) {

            throw new ConflictException("Interviewee does not belong to the provided interviewing process");
        }

        Interviewer interviewer = interviewerService.getInterviewerById(interviewPayload.getInterviewerId());
        if (interviewer == null) {
            throw new NotFoundException("Interviewer not found");
        }

        Interview interview = getInterviewById(interviewId);

        return interviewRepository.save(interviewPayload.toEntity(interview, interviewer));
    }


}
