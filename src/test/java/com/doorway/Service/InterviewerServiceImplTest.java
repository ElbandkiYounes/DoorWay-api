package com.doorway.Service;

import com.doorway.Exception.FileException;
import com.doorway.Exception.InvalidArgumentException;
import com.doorway.Exception.NotFoundException;
import com.doorway.Model.Interviewer;
import com.doorway.Model.Role;
import com.doorway.Payload.InterviewerPayload;
import com.doorway.Repository.InterviewerRepository;
import com.doorway.Service.Interface.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InterviewerServiceImplTest {

    @Mock
    private InterviewerRepository interviewerRepository;

    @Mock
    private RoleService roleService;

    @Mock
    private MultipartFile image;

    @Mock
    private InterviewerPayload mockPayload;

    @InjectMocks
    private InterviewerServiceImpl interviewerService;

    private InterviewerPayload payload;
    private Interviewer interviewer;
    private Role role;
    private Interviewer exludedInterviewer;
    private UUID excludeId;

    @BeforeEach
    void setUp() {
        payload = new InterviewerPayload();
        payload.setName("John Doe");
        payload.setEmail("john.doe@example.com");
        payload.setPhoneNumber("1234567890");
        payload.setPassword("password");
        payload.setRoleId(1L);

        interviewer = Interviewer.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .password("password")
                .role(new Role())
                .build();

        role = new Role();
        role.setId(1L);
        role.setName("Interviewer");

        excludeId = UUID.randomUUID();
        exludedInterviewer = Interviewer.builder()
                .id(excludeId)
                .email("test@example.com")
                .phoneNumber("1234567890")
                .build();
    }

    @Test
    void createInterviewer_Success() {
        // Mock image validation
        when(image.getSize()).thenReturn(4 * 1024 * 1024L); // 4MB
        when(image.getContentType()).thenReturn("image/jpeg");

        // Mock role retrieval
        when(roleService.getRole(payload.getRoleId())).thenReturn(role);

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
    void createInterviewer_EmailAlreadyExists() {
        when(interviewerRepository.existsByEmail(payload.getEmail())).thenReturn(true);

        InvalidArgumentException exception = assertThrows(InvalidArgumentException.class, () -> interviewerService.createInterviewer(payload, image));

        assertEquals("Email already exists", exception.getMessage());
        verify(interviewerRepository, never()).save(any(Interviewer.class));
    }

    @Test
    void createInterviewer_PhoneNumberAlreadyExists() {
        when(interviewerRepository.existsByPhoneNumber(payload.getPhoneNumber())).thenReturn(true);

        InvalidArgumentException exception = assertThrows(InvalidArgumentException.class, () -> interviewerService.createInterviewer(payload, image));

        assertEquals("Phone number already exists", exception.getMessage());
        verify(interviewerRepository, never()).save(any(Interviewer.class));
    }

    @Test
    void createInterviewer_ImageSizeExceedsLimit() {
        when(image.getSize()).thenReturn(6 * 1024 * 1024L); // 6MB

        FileException exception = assertThrows(FileException.class, () -> interviewerService.createInterviewer(payload, image));

        assertEquals("Image size exceeds the maximum allowed size of 5MB.", exception.getMessage());
        verify(interviewerRepository, never()).save(any(Interviewer.class));
    }

    @Test
    void createInterviewer_InvalidImageType() {
        when(image.getSize()).thenReturn(4 * 1024 * 1024L); // 4MB
        when(image.getContentType()).thenReturn("image/gif");

        FileException exception = assertThrows(FileException.class, () -> interviewerService.createInterviewer(payload, image));

        assertEquals("Only JPEG and PNG images are allowed.", exception.getMessage());
        verify(interviewerRepository, never()).save(any(Interviewer.class));
    }

    @Test
    void createInterviewer_RoleNotFound() {
        when(image.getSize()).thenReturn(4 * 1024 * 1024L); // 4MB
        when(image.getContentType()).thenReturn("image/jpeg");
        when(roleService.getRole(payload.getRoleId())).thenReturn(null);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> interviewerService.createInterviewer(payload, image));

        assertEquals("Role not found with ID: " + payload.getRoleId(), exception.getMessage());
        verify(interviewerRepository, never()).save(any(Interviewer.class));
    }

    @Test
    void createInterviewer_ImageProcessingFailure() throws IOException {
        // Use a spy on payload to throw IOException when toEntity is called
        InterviewerPayload spyPayload = Mockito.spy(payload);

        when(image.getSize()).thenReturn(4 * 1024 * 1024L); // 4MB
        when(image.getContentType()).thenReturn("image/jpeg");
        when(roleService.getRole(payload.getRoleId())).thenReturn(role);

        doThrow(new IOException("Image processing error")).when(spyPayload).toEntity(any(MultipartFile.class), any(Role.class));

        FileException exception = assertThrows(FileException.class, () -> interviewerService.createInterviewer(spyPayload, image));

        assertEquals("Failed to process the image: Image processing error", exception.getMessage());
        verify(interviewerRepository, never()).save(any(Interviewer.class));
    }

    // --- Get Interviewer by ID ---
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

        NotFoundException exception = assertThrows(NotFoundException.class, () -> interviewerService.getInterviewerById(id));

        assertEquals("Interviewer not found with ID: " + id, exception.getMessage());
        verify(interviewerRepository, times(1)).findById(id);
    }

    // --- Get All Interviewers ---
    @Test
    void getAllInterviewers_Success() {
        List<Interviewer> interviewers = Collections.singletonList(interviewer);
        when(interviewerRepository.findAll()).thenReturn(interviewers);

        List<Interviewer> result = interviewerService.getAllInterviewers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(interviewer.getName(), result.getFirst().getName());

        verify(interviewerRepository, times(1)).findAll();
    }

    // --- Update Interviewer ---
    @Test
    void updateInterviewer_Success() {
        UUID id = UUID.randomUUID();
        Interviewer existingInterviewer = Interviewer.builder()
                .id(id)
                .name("John Doe")
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .password("password")
                .role(role)
                .build();

        when(interviewerRepository.findById(id)).thenReturn(Optional.of(existingInterviewer));
        when(image.getSize()).thenReturn(4 * 1024 * 1024L); // 4MB
        when(image.getContentType()).thenReturn("image/jpeg");
        when(roleService.getRole(payload.getRoleId())).thenReturn(role);
        when(interviewerRepository.save(any(Interviewer.class))).thenReturn(existingInterviewer);

        Interviewer result = interviewerService.updateInterviewer(id, payload, image);

        assertNotNull(result);
        assertEquals(existingInterviewer.getName(), result.getName());
        assertEquals(existingInterviewer.getEmail(), result.getEmail());

        verify(interviewerRepository, times(1)).findById(id);
        verify(interviewerRepository, times(1)).save(any(Interviewer.class));
    }

    @Test
    void updateInterviewer_EmailAlreadyExists() {
        UUID id = UUID.randomUUID();
        Interviewer existingInterviewer = Interviewer.builder()
                .id(id)
                .name("John Doe")
                .email("old.email@example.com")  // Different email
                .phoneNumber("1234567890")
                .password("password")
                .role(role)
                .build();

        when(interviewerRepository.findById(id)).thenReturn(Optional.of(existingInterviewer));
        when(interviewerRepository.existsByEmail(payload.getEmail())).thenReturn(true);

        FileException exception = assertThrows(FileException.class, () -> interviewerService.updateInterviewer(id, payload, image));

        assertEquals("Email already exists", exception.getMessage());
        verify(interviewerRepository, never()).save(any(Interviewer.class));
    }

    @Test
    void updateInterviewer_PhoneNumberAlreadyExists() {
        UUID id = UUID.randomUUID();
        Interviewer existingInterviewer = Interviewer.builder()
                .id(id)
                .name("John Doe")
                .email("john.doe@example.com")
                .phoneNumber("9876543210")  // Different phone number
                .password("password")
                .role(role)
                .build();

        when(interviewerRepository.findById(id)).thenReturn(Optional.of(existingInterviewer));
        when(interviewerRepository.existsByPhoneNumber(payload.getPhoneNumber())).thenReturn(true);

        InvalidArgumentException exception = assertThrows(InvalidArgumentException.class, () -> interviewerService.updateInterviewer(id, payload, image));

        assertEquals("Phone number already exists", exception.getMessage());
        verify(interviewerRepository, never()).save(any(Interviewer.class));
    }

    @Test
    void updateInterviewer_ImageSizeExceedsLimit() {
        UUID id = UUID.randomUUID();
        when(interviewerRepository.findById(id)).thenReturn(Optional.of(interviewer));
        when(image.getSize()).thenReturn(6 * 1024 * 1024L); // 6MB

        FileException exception = assertThrows(FileException.class, () -> interviewerService.updateInterviewer(id, payload, image));

        assertEquals("Image size exceeds the maximum allowed size of 5MB.", exception.getMessage());
        verify(interviewerRepository, never()).save(any(Interviewer.class));
    }

    @Test
    void updateInterviewer_InvalidImageType() {
        UUID id = UUID.randomUUID();
        when(interviewerRepository.findById(id)).thenReturn(Optional.of(interviewer));
        when(image.getSize()).thenReturn(4 * 1024 * 1024L); // 4MB
        when(image.getContentType()).thenReturn("image/gif");

        FileException exception = assertThrows(FileException.class, () -> interviewerService.updateInterviewer(id, payload, image));

        assertEquals("Only JPEG and PNG images are allowed.", exception.getMessage());
        verify(interviewerRepository, never()).save(any(Interviewer.class));
    }

    @Test
    void updateInterviewer_RoleNotFound() {
        UUID id = UUID.randomUUID();
        when(interviewerRepository.findById(id)).thenReturn(Optional.of(interviewer));
        when(image.getSize()).thenReturn(4 * 1024 * 1024L); // 4MB
        when(image.getContentType()).thenReturn("image/jpeg");
        when(roleService.getRole(payload.getRoleId())).thenReturn(null);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> interviewerService.updateInterviewer(id, payload, image));

        assertEquals("Role not found with ID: " + payload.getRoleId(), exception.getMessage());
        verify(interviewerRepository, never()).save(any(Interviewer.class));
    }

    @Test
    void updateInterviewer_ImageProcessingFailure() throws IOException {
        UUID id = UUID.randomUUID();
        InterviewerPayload spyPayload = Mockito.spy(payload);

        when(interviewerRepository.findById(id)).thenReturn(Optional.of(interviewer));
        when(image.getSize()).thenReturn(4 * 1024 * 1024L); // 4MB
        when(image.getContentType()).thenReturn("image/jpeg");
        when(roleService.getRole(spyPayload.getRoleId())).thenReturn(role);

        doThrow(new IOException("Image processing error")).when(spyPayload).toEntity(any(Interviewer.class), any(MultipartFile.class), any(Role.class));

        FileException exception = assertThrows(FileException.class, () -> interviewerService.updateInterviewer(id, spyPayload, image));

        assertEquals("Failed to process the image: Image processing error", exception.getMessage());
        verify(interviewerRepository, never()).save(any(Interviewer.class));
    }

    @Test
    void updateInterviewer_InterviewerNotFound() {
        UUID id = UUID.randomUUID();
        when(interviewerRepository.findById(id)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> interviewerService.updateInterviewer(id, payload, image));

        assertEquals("Interviewer not found with ID: " + id, exception.getMessage());
        verify(interviewerRepository, never()).save(any(Interviewer.class));
    }

    // --- Delete Interviewer ---
    @Test
    void deleteInterviewer_Success() {
        UUID id = UUID.randomUUID();
        when(interviewerRepository.findById(id)).thenReturn(Optional.of(interviewer));
        doNothing().when(interviewerRepository).deleteById(id);

        assertDoesNotThrow(() -> interviewerService.deleteInterviewer(id));

        verify(interviewerRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteInterviewer_NotFound() {
        UUID id = UUID.randomUUID();
        when(interviewerRepository.findById(id)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> interviewerService.deleteInterviewer(id));

        assertEquals("Interviewer not found with ID: " + id, exception.getMessage());
        verify(interviewerRepository, never()).deleteById(id);
    }

    @Test
    void getInterviewerByEmail_ShouldReturnTrue_WhenEmailExists() {
        when(interviewerRepository.existsByEmail("test@example.com")).thenReturn(true);

        Boolean result = interviewerService.getInterviewerByEmail("test@example.com", null);

        assertTrue(result);
        verify(interviewerRepository, times(1)).existsByEmail("test@example.com");
    }

    @Test
    void getInterviewerByEmail_ShouldReturnFalse_WhenEmailDoesNotExist() {
        when(interviewerRepository.existsByEmail("test@example.com")).thenReturn(false);

        Boolean result = interviewerService.getInterviewerByEmail("test@example.com", null);

        assertFalse(result);
        verify(interviewerRepository, times(1)).existsByEmail("test@example.com");
    }

    @Test
    void getInterviewerByEmail_ShouldThrowNotFoundException_WhenExcludeIdNotFound() {
        when(interviewerRepository.findById(excludeId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                interviewerService.getInterviewerByEmail("test@example.com", excludeId));

        assertEquals("Interviewer not found", exception.getMessage());
        verify(interviewerRepository, times(1)).findById(excludeId);
        verify(interviewerRepository, never()).existsByEmail(anyString());
    }

    @Test
    void getInterviewerByPhone_ShouldReturnFalse_WhenPhoneNumberMatchesExcludeId() {
        when(interviewerRepository.findById(excludeId)).thenReturn(Optional.of(interviewer));

        Boolean result = interviewerService.getInterviewerByPhone("1234567890", excludeId);

        assertFalse(result);
        verify(interviewerRepository, times(1)).findById(excludeId);
        verify(interviewerRepository, never()).existsByPhoneNumber(anyString());
    }

    @Test
    void getInterviewerByPhone_ShouldThrowNotFoundException_WhenExcludeIdNotFound() {
        when(interviewerRepository.findById(excludeId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                interviewerService.getInterviewerByPhone("1234567890", excludeId));

        assertEquals("Interviewer not found", exception.getMessage());
        verify(interviewerRepository, times(1)).findById(excludeId);
        verify(interviewerRepository, never()).existsByPhoneNumber(anyString());
    }
}