package com.doorway.Service;

import com.doorway.Exception.FileException;
import com.doorway.Exception.InvalidArgumentException;
import com.doorway.Exception.NotFoundException;
import com.doorway.Model.Interviewer;
import com.doorway.Model.Role;
import com.doorway.Payload.InterviewerPayload;
import com.doorway.Repository.InterviewerRepository;
import com.doorway.Service.Interface.InterviewerService;
import com.doorway.Service.Interface.RoleService;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    public InterviewerServiceImpl(InterviewerRepository interviewerRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.interviewerRepository = interviewerRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    // Create Interviewer
    public Interviewer createInterviewer(InterviewerPayload payload, MultipartFile image) {
        if (interviewerRepository.existsByEmail(payload.getEmail())) {
            throw new InvalidArgumentException("Email already exists");
        }
        if (interviewerRepository.existsByPhoneNumber(payload.getPhoneNumber())) {
            throw new InvalidArgumentException("Phone number already exists");
        }

        // Validate image size
        validateImage(image);

        Role role = roleService.getRole(payload.getRoleId());
        if (role == null) {
            throw new NotFoundException("Role not found with ID: " + payload.getRoleId());
        }

        try {
            Interviewer interviewer = payload.toEntity(image, role);
            interviewer.setPassword(passwordEncoder.encode(interviewer.getPassword()));
            return interviewerRepository.save(interviewer);
        } catch (IOException e) {
            throw new FileException("Failed to process the image: " + e.getMessage());
        }
    }

    // Get Interviewer by ID
    public Interviewer getInterviewerById(UUID id) {
        return interviewerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Interviewer not found with ID: " + id));
    }

    // Get all Interviewers
    public List<Interviewer> getAllInterviewers() {
        return interviewerRepository.findAll();
    }

    // Update Interviewer
    public Interviewer updateInterviewer(UUID id, InterviewerPayload payload, MultipartFile image) {
        // Get the current Interviewer details
        Interviewer existingInterviewer = getInterviewerById(id);

        // Check if email or phone number was updated, only if different from existing values
        if (!existingInterviewer.getEmail().equals(payload.getEmail()) && interviewerRepository.existsByEmail(payload.getEmail())) {
            throw new FileException("Email already exists");
        }
        if (!existingInterviewer.getPhoneNumber().equals(payload.getPhoneNumber()) && interviewerRepository.existsByPhoneNumber(payload.getPhoneNumber())) {
            throw new InvalidArgumentException("Phone number already exists");
        }

        // Validate image size
        validateImage(image);

        // Get the role and check if it exists
        Role role = roleService.getRole(payload.getRoleId());
        if (role == null) {
            throw new NotFoundException("Role not found with ID: " + payload.getRoleId());
        }

        try {
            // Update the Interviewer entity
            existingInterviewer = payload.toEntity(existingInterviewer, image, role);
            return interviewerRepository.save(existingInterviewer);
        } catch (IOException e) {
            throw new FileException("Failed to process the image: " + e.getMessage());
        }
    }

    // Delete Interviewer
    public void deleteInterviewer(UUID id) {
        getInterviewerById(id);
        interviewerRepository.deleteById(id);
    }

    @Override
    public Boolean getInterviewerByPhone(String phoneNumber, UUID excludeId) {
        if (excludeId != null) {
            Interviewer interviewer = interviewerRepository.findById(excludeId)
                    .orElseThrow(() -> new NotFoundException("Interviewer not found"));
            if (interviewer.getPhoneNumber().equals(phoneNumber)) {
                return false;
            }
        }
        return interviewerRepository.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public Boolean getInterviewerByEmail(String email, UUID excludeId) {
        if (excludeId != null) {
            Interviewer interviewer = interviewerRepository.findById(excludeId)
                    .orElseThrow(() -> new NotFoundException("Interviewer not found"));
            if (interviewer.getEmail().equals(email)) {
                return false;
            }
        }
        return interviewerRepository.existsByEmail(email);
    }

    // Helper method to validate the image
    private void validateImage(MultipartFile image) {
        // Validate image size
        if (image.getSize() > MAX_FILE_SIZE) {
            throw new FileException("Image size exceeds the maximum allowed size of 5MB.");
        }

        // Validate image type
        if (!ALLOWED_FILE_TYPES.contains(image.getContentType())) {
            throw new FileException("Only JPEG and PNG images are allowed.");
        }
    }
}
