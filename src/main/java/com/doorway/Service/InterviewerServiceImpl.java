package com.doorway.Service;

import com.doorway.Exception.FileException;
import com.doorway.Exception.NotFoundException;
import com.doorway.Model.Interviewer;
import com.doorway.Model.Role;
import com.doorway.Payload.InterviewerPayload;
import com.doorway.Repository.InterviewerRepository;
import com.doorway.Service.Interface.InterviewerService;
import com.doorway.Service.Interface.RoleService;
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
    private final RoleService roleService;

    public InterviewerServiceImpl(InterviewerRepository interviewerRepository, RoleService roleService) {
        this.interviewerRepository = interviewerRepository;
        this.roleService = roleService;
    }

    //Create Interviewer
    public Interviewer createInterviewer(InterviewerPayload payload, MultipartFile image){
        // Validate image size
        if (image.getSize() > MAX_FILE_SIZE) {
            throw new FileException("Image size exceeds the maximum allowed size of 5MB.");
        }

        // Validate image type
        if (!ALLOWED_FILE_TYPES.contains(image.getContentType())) {
            throw new FileException("Only JPEG and PNG images are allowed.");
        }

        Role role = roleService.getRole(payload.getRoleId());
        if(role == null){
            throw new NotFoundException("Role not found with ID: " + payload.getRoleId());
        }

        try {
            Interviewer interviewer = payload.toEntity(image,role);
            return interviewerRepository.save(interviewer);
        } catch (IOException e) {
            throw new FileException("Failed to process the image: " + e.getMessage());
        }
    }


    //Get Interviewer by ID
    public Interviewer getInterviewerById(UUID id){
        return interviewerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Interviewer not found with ID: " + id));
    }

    //Get all Interviewers
    public List<Interviewer> getAllInterviewers() {
        return interviewerRepository.findAll();
    }


    //Update Interviewer
    public Interviewer updateInterviewer(UUID id, InterviewerPayload payload, MultipartFile image){
        // Validate image size
        if (image.getSize() > MAX_FILE_SIZE) {
            throw new FileException("Image size exceeds the maximum allowed size of 5MB.");
        }

        // Validate image type
        if (!ALLOWED_FILE_TYPES.contains(image.getContentType())) {
            throw new FileException("Only JPEG and PNG images are allowed.");
        }
        Role role = roleService.getRole(payload.getRoleId());
        if(role == null){
            throw new NotFoundException("Role not found with ID: " + payload.getRoleId());
        }

        try {
            Interviewer interviewer = getInterviewerById(id);
            interviewer = payload.toEntity(interviewer, image, role);
            return interviewerRepository.save(interviewer);
        } catch (IOException e) {
            throw new FileException("Failed to process the image: " + e.getMessage());
        }
    }


    //Delete Interviewer
    public void deleteInterviewer(UUID id){
        interviewerRepository.deleteById(id);
    }
}