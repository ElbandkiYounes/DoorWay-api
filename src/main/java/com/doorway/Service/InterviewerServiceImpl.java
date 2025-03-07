package com.doorway.Service;

import com.doorway.Exception.ImageException;
import com.doorway.Exception.NotFoundException;
import com.doorway.Model.Interviewer;
import com.doorway.Payload.InterviewerPayload;
import com.doorway.Repository.InterviewerRepository;
import com.doorway.Service.Inteface.InterviewerService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class InterviewerServiceImpl implements InterviewerService {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final List<String> ALLOWED_FILE_TYPES = List.of("image/jpeg", "image/png");

    private final InterviewerRepository interviewerRepository;

    public InterviewerServiceImpl(InterviewerRepository interviewerRepository) {
        this.interviewerRepository = interviewerRepository;
    }

    public Interviewer createInterviewer(InterviewerPayload payload, MultipartFile image){
        // Validate image size
        if (image.getSize() > MAX_FILE_SIZE) {
            throw new ImageException("Image size exceeds the maximum allowed size of 5MB.");
        }

        // Validate image type
        if (!ALLOWED_FILE_TYPES.contains(image.getContentType())) {
            throw new ImageException("Only JPEG and PNG images are allowed.");
        }

        try {
            Interviewer interviewer = payload.toEntity(image);
            return interviewerRepository.save(interviewer);
        } catch (IOException e) {
            throw new ImageException("Failed to process the image: " + e.getMessage());
        }
    }

    public Interviewer getInterviewerById(UUID id){
        return interviewerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Interviewer not found with ID: " + id));
    }

    public List<Interviewer> getAllInterviewers() {
        return interviewerRepository.findAll();
    }

    public Interviewer updateInterviewer(UUID id, InterviewerPayload payload, MultipartFile image){
        // Validate image size
        if (image.getSize() > MAX_FILE_SIZE) {
            throw new ImageException("Image size exceeds the maximum allowed size of 5MB.");
        }

        // Validate image type
        if (!ALLOWED_FILE_TYPES.contains(image.getContentType())) {
            throw new ImageException("Only JPEG and PNG images are allowed.");
        }

        try {
            Interviewer interviewer = interviewerRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Interviewer not found with ID: " + id));
            interviewer = payload.toEntity(interviewer, image);
            return interviewerRepository.save(interviewer);
        } catch (IOException e) {
            throw new ImageException("Failed to process the image: " + e.getMessage());
        }
    }

    public void deleteInterviewer(UUID id){
        interviewerRepository.deleteById(id);
    }
}