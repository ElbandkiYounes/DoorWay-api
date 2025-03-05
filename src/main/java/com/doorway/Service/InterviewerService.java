package com.doorway.Service;

import com.doorway.Exception.NotFoundException;
import com.doorway.Model.Interviewer;
import com.doorway.Payload.InterviewerPayload;
import com.doorway.Repository.InterviewerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class InterviewerService {

    private final InterviewerRepository interviewerRepository;

    public InterviewerService(InterviewerRepository interviewerRepository) {
        this.interviewerRepository = interviewerRepository;
    }

    public Interviewer createInterviewer(InterviewerPayload payload) {
        Interviewer interviewer = payload.toEntity();
        return interviewerRepository.save(interviewer);
    }

    public Interviewer getInterviewerById(UUID id) throws NotFoundException {
        return interviewerRepository.findById(id).orElseThrow(() -> new NotFoundException("Interviewer not found with ID: " + id));
    }

    public List<Interviewer> getAllInterviewers() {
        return interviewerRepository.findAll();
    }

    public Interviewer updateInterviewer(UUID id, InterviewerPayload payload) {
        Interviewer interviewer = interviewerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Interviewer not found with ID: " + id));
        interviewer = payload.toEntity(interviewer);
        return interviewerRepository.save(interviewer);
    }
}