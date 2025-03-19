package com.doorway.Service;

import com.doorway.Exception.FileException;
import com.doorway.Exception.InvalidArgumentException;
import com.doorway.Exception.NotFoundException;
import com.doorway.Model.Decision;
import com.doorway.Model.Interviewee;
import com.doorway.Model.School;
import com.doorway.Payload.IntervieweePayload;
import com.doorway.Repository.IntervieweeRepository;
import com.doorway.Service.Interface.IntervieweeService;
import com.doorway.Service.Interface.SchoolService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class IntervieweeServiceImpl implements IntervieweeService {

    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final long MAX_RESUME_SIZE = 10 * 1024 * 1024; // 10MB
    private static final List<String> ALLOWED_IMAGE_TYPES = List.of("image/jpeg", "image/png");
    private static final List<String> ALLOWED_RESUME_TYPES = List.of("application/pdf");

    private final IntervieweeRepository intervieweeRepository;
    private final SchoolService schoolService;

    public IntervieweeServiceImpl(IntervieweeRepository intervieweeRepository, SchoolService schoolService) {
        this.intervieweeRepository = intervieweeRepository;
        this.schoolService = schoolService;
    }

    // Get Interviewee by ID
    public Interviewee getIntervieweeById(UUID id) {
        return intervieweeRepository.findById(id).orElseThrow(() -> new NotFoundException("Interviewee not found"));
    }

    // Get all Interviewees
    public List<Interviewee> getAllInterviewees(Decision decision) {
        if (decision != null) {
            return intervieweeRepository.findAllByInterviewingProcesses_Decision(decision);
        }
        return intervieweeRepository.findAll();
    }

    // Save Interviewee
    public Interviewee saveInterviewee(IntervieweePayload intervieweePayload, MultipartFile image, MultipartFile resume) {
        if (intervieweeRepository.existsByEmail(intervieweePayload.getEmail())) {
            throw new InvalidArgumentException("Email already exists");
        }
        if (intervieweeRepository.existsByPhoneNumber(intervieweePayload.getPhoneNumber())) {
            throw new InvalidArgumentException("Phone number already exists");
        }
        // Validate image size
        validateImage(image);

        // Validate resume size
        validateResume(resume);

        School school = schoolService.getSchool(intervieweePayload.getSchoolId());
        if (school == null) {
            throw new NotFoundException("School not found");
        }

        try {
            return intervieweeRepository.save(intervieweePayload.toEntity(image, resume, school));
        } catch (IOException e) {
            throw new FileException("Failed to process the image or resume: " + e.getMessage());
        }
    }

    // Update Interviewee
    public Interviewee updateInterviewee(UUID id, IntervieweePayload intervieweePayload, MultipartFile image, MultipartFile resume) {
        Interviewee existingInterviewee = getIntervieweeById(id);

        // Check if email or phone number was updated, only if different from existing values
        if (!existingInterviewee.getEmail().equals(intervieweePayload.getEmail()) && intervieweeRepository.existsByEmail(intervieweePayload.getEmail())) {
            throw new InvalidArgumentException("Email already exists");
        }
        if (!existingInterviewee.getPhoneNumber().equals(intervieweePayload.getPhoneNumber()) && intervieweeRepository.existsByPhoneNumber(intervieweePayload.getPhoneNumber())) {
            throw new InvalidArgumentException("Phone number already exists");
        }

        // Validate image size
        validateImage(image);

        // Validate resume size
        validateResume(resume);

        School school = schoolService.getSchool(intervieweePayload.getSchoolId());
        if (school == null) {
            throw new NotFoundException("School not found");
        }

        try {
            // Update the Interviewee entity
            return intervieweeRepository.save(intervieweePayload.toEntity(existingInterviewee, image, resume, school));
        } catch (IOException e) {
            throw new FileException("Failed to process the image or resume: " + e.getMessage());
        }
    }

    // Delete Interviewee
    public void deleteInterviewee(UUID id) {
        Interviewee interviewee = getIntervieweeById(id);
        intervieweeRepository.delete(interviewee);
    }

    @Override
    public Boolean getIntervieweeByPhone(String phone, UUID excludeId) {
        if (excludeId != null) {
            Interviewee interviewee = intervieweeRepository.findById(excludeId)
                    .orElseThrow(() -> new NotFoundException("Interviewee not found"));
            if (interviewee.getPhoneNumber().equals(phone)) {
                return false;
            }
        }
        return intervieweeRepository.existsByPhoneNumber(phone);
    }

    @Override
    public Boolean getIntervieweeByEmail(String email, UUID excludeId) {
        if (excludeId != null) {
            Interviewee interviewee = intervieweeRepository.findById(excludeId)
                    .orElseThrow(() -> new NotFoundException("Interviewee not found"));
            if (interviewee.getEmail().equals(email)) {
                return false;
            }
        }
        return intervieweeRepository.existsByEmail(email);
    }

    // Helper method to validate the image
    private void validateImage(MultipartFile image) {
        if (image.getSize() > MAX_IMAGE_SIZE) {
            throw new FileException("Image size exceeds the maximum allowed size of 5MB.");
        }

        if (!ALLOWED_IMAGE_TYPES.contains(image.getContentType())) {
            throw new FileException("Only JPEG and PNG images are allowed.");
        }
    }

    // Helper method to validate the resume
    private void validateResume(MultipartFile resume) {
        if (resume.getSize() > MAX_RESUME_SIZE) {
            throw new FileException("Resume size exceeds the maximum allowed size of 10MB.");
        }

        if (!ALLOWED_RESUME_TYPES.contains(resume.getContentType())) {
            throw new FileException("Only PDF files are allowed.");
        }
    }
}
