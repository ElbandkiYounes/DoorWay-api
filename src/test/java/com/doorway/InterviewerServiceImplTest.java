package com.doorway;

import com.doorway.Exception.ImageException;
import com.doorway.Exception.NotFoundException;
import com.doorway.Model.Interviewer;
import com.doorway.Payload.InterviewerPayload;
import com.doorway.Repository.InterviewerRepository;
import com.doorway.Service.InterviewerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InterviewerServiceImplTest {

    @Mock
    private InterviewerRepository interviewerRepository;

    @Mock
    private MultipartFile image;

    @InjectMocks
    private InterviewerServiceImpl interviewerService;

    private InterviewerPayload payload;
    private Interviewer interviewer;

    @BeforeEach
    void setUp() {
        payload = new InterviewerPayload();
        payload.setName("John Doe");
        payload.setEmail("john.doe@example.com");
        payload.setPhoneNumber("1234567890");
        payload.setPassword("password");
        payload.setRole("Interviewer");

        interviewer = Interviewer.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .password("password")
                .role("Interviewer")
                .build();
    }

    @Test
    void createInterviewer_Success(){
        // Mock image validation
        when(image.getSize()).thenReturn(4 * 1024 * 1024L); // 4MB
        when(image.getContentType()).thenReturn("image/jpeg");

        // Mock repository save
        when(interviewerRepository.save(any(Interviewer.class))).thenReturn(interviewer);

        // Call the method
        Interviewer result = interviewerService.createInterviewer(payload, image);

        // Verify the result
        assertNotNull(result);
        assertEquals(interviewer.getName(), result.getName());
        assertEquals(interviewer.getEmail(), result.getEmail());

        // Verify repository interaction
        verify(interviewerRepository, times(1)).save(any(Interviewer.class));
    }

    @Test
    void createInterviewer_ImageSizeExceedsLimit() {
        // Mock image validation
        when(image.getSize()).thenReturn(6 * 1024 * 1024L); // 6MB

        // Call the method and expect an exception
        ImageException exception = assertThrows(ImageException.class, () -> {
            interviewerService.createInterviewer(payload, image);
        });

        // Verify the exception message
        assertEquals("Image size exceeds the maximum allowed size of 5MB.", exception.getMessage());

        // Verify no repository interaction
        verify(interviewerRepository, never()).save(any(Interviewer.class));
    }

    @Test
    void createInterviewer_InvalidImageType() {
        // Mock image validation
        when(image.getSize()).thenReturn(4 * 1024 * 1024L); // 4MB
        when(image.getContentType()).thenReturn("image/gif");

        // Call the method and expect an exception
        ImageException exception = assertThrows(ImageException.class, () -> {
            interviewerService.createInterviewer(payload, image);
        });

        // Verify the exception message
        assertEquals("Only JPEG and PNG images are allowed.", exception.getMessage());

        // Verify no repository interaction
        verify(interviewerRepository, never()).save(any(Interviewer.class));
    }

    @Test
    void getInterviewerById_Success() {
        UUID id = UUID.randomUUID();
        when(interviewerRepository.findById(id)).thenReturn(Optional.of(interviewer));

        Interviewer result = interviewerService.getInterviewerById(id);

        assertNotNull(result);
        assertEquals(interviewer.getName(), result.getName());
        assertEquals(interviewer.getEmail(), result.getEmail());

        verify(interviewerRepository, times(1)).findById(id);
    }

    @Test
    void getInterviewerById_NotFound() {
        UUID id = UUID.randomUUID();
        when(interviewerRepository.findById(id)).thenReturn(Optional.empty());

        // Call the method and expect an exception
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            interviewerService.getInterviewerById(id);
        });

        // Verify the exception message
        assertEquals("Interviewer not found with ID: " + id, exception.getMessage());

        // Verify repository interaction
        verify(interviewerRepository, times(1)).findById(id);
    }

    @Test
    void getAllInterviewers_Success() {
        List<Interviewer> interviewers = Arrays.asList(interviewer);
        when(interviewerRepository.findAll()).thenReturn(interviewers);

        List<Interviewer> result = interviewerService.getAllInterviewers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(interviewer.getName(), result.get(0).getName());

        verify(interviewerRepository, times(1)).findAll();
    }

    @Test
    void updateInterviewer_Success() throws IOException {
        UUID id = UUID.randomUUID();
        when(interviewerRepository.findById(id)).thenReturn(Optional.of(interviewer));
        when(image.getSize()).thenReturn(4 * 1024 * 1024L); // 4MB
        when(image.getContentType()).thenReturn("image/jpeg");
        when(interviewerRepository.save(any(Interviewer.class))).thenReturn(interviewer);

        Interviewer result = interviewerService.updateInterviewer(id, payload, image);

        assertNotNull(result);
        assertEquals(interviewer.getName(), result.getName());
        assertEquals(interviewer.getEmail(), result.getEmail());

        verify(interviewerRepository, times(1)).findById(id);
        verify(interviewerRepository, times(1)).save(any(Interviewer.class));
    }

    @Test
    void updateInterviewer_NotFound() {
        UUID id = UUID.randomUUID();
        when(interviewerRepository.findById(id)).thenReturn(Optional.empty());

        // Mock image validation to avoid NullPointerException
        when(image.getSize()).thenReturn(4 * 1024 * 1024L); // 4MB
        when(image.getContentType()).thenReturn("image/jpeg");

        // Call the method and expect an exception
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            interviewerService.updateInterviewer(id, payload, image);
        });

        // Verify the exception message
        assertEquals("Interviewer not found with ID: " + id, exception.getMessage());

        // Verify repository interaction
        verify(interviewerRepository, times(1)).findById(id);
        verify(interviewerRepository, never()).save(any(Interviewer.class));
    }

    @Test
    void deleteInterviewer_Success() {
        UUID id = UUID.randomUUID();
        doNothing().when(interviewerRepository).deleteById(id);

        // Call the method and expect no exception
        assertDoesNotThrow(() -> {
            interviewerService.deleteInterviewer(id);
        });

        // Verify repository interaction
        verify(interviewerRepository, times(1)).deleteById(id);
    }
}