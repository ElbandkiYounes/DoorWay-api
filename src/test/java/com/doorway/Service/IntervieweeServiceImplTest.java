package com.doorway.Service;

import com.doorway.Exception.FileException;
import com.doorway.Exception.InvalidArgumentException;
import com.doorway.Exception.NotFoundException;
import com.doorway.Model.Decision;
import com.doorway.Model.Interviewee;
import com.doorway.Model.School;
import com.doorway.Payload.IntervieweePayload;
import com.doorway.Repository.IntervieweeRepository;
import com.doorway.Service.Interface.SchoolService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IntervieweeServiceImplTest {

    @Mock
    private IntervieweeRepository intervieweeRepository;

    @Mock
    private SchoolService schoolService;

    @InjectMocks
    private IntervieweeServiceImpl intervieweeService;

    private UUID validId;
    private Interviewee mockInterviewee;
    private IntervieweePayload validPayload;
    private MultipartFile validImage;
    private MultipartFile validResume;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup common test data
        validId = UUID.randomUUID();
        mockInterviewee = new Interviewee();
        mockInterviewee.setEmail("test@example.com");
        mockInterviewee.setPhoneNumber("1234567890");

        validPayload = new IntervieweePayload();
        validPayload.setEmail("new@example.com");
        validPayload.setPhoneNumber("0987654321");
        validPayload.setSchoolId(1L);

        School mockSchool = new School();

        validImage = new MockMultipartFile("image", "image.jpg", "image/jpeg", new byte[1024]);
        validResume = new MockMultipartFile("resume", "resume.pdf", "application/pdf", new byte[1024]);

        when(schoolService.getSchool(validPayload.getSchoolId())).thenReturn(mockSchool);
    }

    @Test
    void getIntervieweeById_ShouldReturnInterviewee_WhenFound() {
        when(intervieweeRepository.findById(validId)).thenReturn(Optional.of(mockInterviewee));

        Interviewee result = intervieweeService.getIntervieweeById(validId);

        assertNotNull(result);
        assertEquals(mockInterviewee, result);
        verify(intervieweeRepository, times(1)).findById(validId);
    }

    @Test
    void getIntervieweeById_ShouldThrowNotFoundException_WhenNotFound() {
        when(intervieweeRepository.findById(validId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> intervieweeService.getIntervieweeById(validId));
        verify(intervieweeRepository, times(1)).findById(validId);
    }

    @Test
    void getAllInterviewees_ShouldReturnAllInterviewees_WhenDecisionIsNull() {
        List<Interviewee> interviewees = List.of(new Interviewee(), new Interviewee());
        when(intervieweeRepository.findAll()).thenReturn(interviewees);

        List<Interviewee> result = intervieweeService.getAllInterviewees(null);

        assertNotNull(result);
        assertEquals(interviewees, result);
        assertEquals(2, result.size());
        verify(intervieweeRepository, times(1)).findAll();
        verify(intervieweeRepository, never()).findAllByInterviewingProcesses_Decision(any(Decision.class));
    }

    @Test
    void getAllInterviewees_ShouldReturnIntervieweesByDecision_WhenDecisionIsProvided() {
        Decision decision = Decision.NEUTRAL;
        List<Interviewee> interviewees = List.of(new Interviewee(), new Interviewee());
        when(intervieweeRepository.findAllByInterviewingProcesses_Decision(decision)).thenReturn(interviewees);

        List<Interviewee> result = intervieweeService.getAllInterviewees(decision);

        assertNotNull(result);
        assertEquals(interviewees, result);
        verify(intervieweeRepository, times(1)).findAllByInterviewingProcesses_Decision(decision);
        verify(intervieweeRepository, never()).findAll();
    }

    @Test
    void saveInterviewee_ShouldSaveInterviewee_WhenAllInputsAreValid() {
        when(intervieweeRepository.existsByEmail(validPayload.getEmail())).thenReturn(false);
        when(intervieweeRepository.existsByPhoneNumber(validPayload.getPhoneNumber())).thenReturn(false);
        when(intervieweeRepository.save(any(Interviewee.class))).thenReturn(mockInterviewee);

        Interviewee result = intervieweeService.saveInterviewee(validPayload, validImage, validResume);

        assertNotNull(result);
        assertEquals(mockInterviewee, result);
        verify(intervieweeRepository, times(1)).save(any(Interviewee.class));
        verify(schoolService, times(1)).getSchool(validPayload.getSchoolId());
    }

    @Test
    void saveInterviewee_ShouldThrowInvalidArgumentException_WhenEmailAlreadyExists() {
        when(intervieweeRepository.existsByEmail(validPayload.getEmail())).thenReturn(true);

        InvalidArgumentException exception = assertThrows(InvalidArgumentException.class,
                () -> intervieweeService.saveInterviewee(validPayload, validImage, validResume));

        assertEquals("Email already exists", exception.getMessage());
        verify(intervieweeRepository, never()).save(any(Interviewee.class));
    }

    @Test
    void saveInterviewee_ShouldThrowInvalidArgumentException_WhenPhoneNumberAlreadyExists() {
        when(intervieweeRepository.existsByEmail(validPayload.getEmail())).thenReturn(false);
        when(intervieweeRepository.existsByPhoneNumber(validPayload.getPhoneNumber())).thenReturn(true);

        InvalidArgumentException exception = assertThrows(InvalidArgumentException.class,
                () -> intervieweeService.saveInterviewee(validPayload, validImage, validResume));

        assertEquals("Phone number already exists", exception.getMessage());
        verify(intervieweeRepository, never()).save(any(Interviewee.class));
    }

    @Test
    void saveInterviewee_ShouldThrowNotFoundException_WhenSchoolNotFound() {
        when(intervieweeRepository.existsByEmail(validPayload.getEmail())).thenReturn(false);
        when(intervieweeRepository.existsByPhoneNumber(validPayload.getPhoneNumber())).thenReturn(false);
        when(schoolService.getSchool(validPayload.getSchoolId())).thenReturn(null);

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> intervieweeService.saveInterviewee(validPayload, validImage, validResume));

        assertEquals("School not found", exception.getMessage());
        verify(intervieweeRepository, never()).save(any(Interviewee.class));
    }

    @Test
    void saveInterviewee_ShouldThrowFileException_WhenImageSizeExceedsLimit() {
        when(intervieweeRepository.existsByEmail(validPayload.getEmail())).thenReturn(false);
        when(intervieweeRepository.existsByPhoneNumber(validPayload.getPhoneNumber())).thenReturn(false);

        MultipartFile oversizedImage = new MockMultipartFile("image", "image.jpg", "image/jpeg", new byte[6 * 1024 * 1024]);

        FileException exception = assertThrows(FileException.class,
                () -> intervieweeService.saveInterviewee(validPayload, oversizedImage, validResume));

        assertEquals("Image size exceeds the maximum allowed size of 5MB.", exception.getMessage());
        verify(intervieweeRepository, never()).save(any(Interviewee.class));
    }

    @Test
    void saveInterviewee_ShouldThrowFileException_WhenImageTypeIsInvalid() {
        when(intervieweeRepository.existsByEmail(validPayload.getEmail())).thenReturn(false);
        when(intervieweeRepository.existsByPhoneNumber(validPayload.getPhoneNumber())).thenReturn(false);

        MultipartFile invalidImageType = new MockMultipartFile("image", "image.gif", "image/gif", new byte[1024]);

        FileException exception = assertThrows(FileException.class,
                () -> intervieweeService.saveInterviewee(validPayload, invalidImageType, validResume));

        assertEquals("Only JPEG and PNG images are allowed.", exception.getMessage());
        verify(intervieweeRepository, never()).save(any(Interviewee.class));
    }

    @Test
    void saveInterviewee_ShouldThrowFileException_WhenResumeSizeExceedsLimit() {
        when(intervieweeRepository.existsByEmail(validPayload.getEmail())).thenReturn(false);
        when(intervieweeRepository.existsByPhoneNumber(validPayload.getPhoneNumber())).thenReturn(false);

        MultipartFile oversizedResume = new MockMultipartFile("resume", "resume.pdf", "application/pdf", new byte[11 * 1024 * 1024]);

        FileException exception = assertThrows(FileException.class,
                () -> intervieweeService.saveInterviewee(validPayload, validImage, oversizedResume));

        assertEquals("Resume size exceeds the maximum allowed size of 10MB.", exception.getMessage());
        verify(intervieweeRepository, never()).save(any(Interviewee.class));
    }

    @Test
    void saveInterviewee_ShouldThrowFileException_WhenResumeTypeIsInvalid() {
        when(intervieweeRepository.existsByEmail(validPayload.getEmail())).thenReturn(false);
        when(intervieweeRepository.existsByPhoneNumber(validPayload.getPhoneNumber())).thenReturn(false);

        MultipartFile invalidResumeType = new MockMultipartFile("resume", "resume.doc", "application/msword", new byte[1024]);

        FileException exception = assertThrows(FileException.class,
                () -> intervieweeService.saveInterviewee(validPayload, validImage, invalidResumeType));

        assertEquals("Only PDF files are allowed.", exception.getMessage());
        verify(intervieweeRepository, never()).save(any(Interviewee.class));
    }

    @Test
    void updateInterviewee_ShouldUpdateInterviewee_WhenAllInputsAreValid() {
        when(intervieweeRepository.findById(validId)).thenReturn(Optional.of(mockInterviewee));
        when(intervieweeRepository.save(any(Interviewee.class))).thenReturn(mockInterviewee);

        Interviewee result = intervieweeService.updateInterviewee(validId, validPayload, validImage, validResume);

        assertNotNull(result);
        assertEquals(mockInterviewee, result);
        verify(intervieweeRepository, times(1)).save(any(Interviewee.class));
        verify(schoolService, times(1)).getSchool(validPayload.getSchoolId());
    }

    @Test
    void updateInterviewee_ShouldThrowNotFoundException_WhenIntervieweeNotFound() {
        when(intervieweeRepository.findById(validId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> intervieweeService.updateInterviewee(validId, validPayload, validImage, validResume));

        verify(intervieweeRepository, never()).save(any(Interviewee.class));
    }

    @Test
    void updateInterviewee_ShouldThrowInvalidArgumentException_WhenEmailAlreadyExists() {
        // Setup a different email for the update
        validPayload.setEmail("different@example.com");

        when(intervieweeRepository.findById(validId)).thenReturn(Optional.of(mockInterviewee));
        when(intervieweeRepository.existsByEmail(validPayload.getEmail())).thenReturn(true);

        InvalidArgumentException exception = assertThrows(InvalidArgumentException.class,
                () -> intervieweeService.updateInterviewee(validId, validPayload, validImage, validResume));

        assertEquals("Email already exists", exception.getMessage());
        verify(intervieweeRepository, never()).save(any(Interviewee.class));
    }

    @Test
    void updateInterviewee_ShouldThrowInvalidArgumentException_WhenPhoneNumberAlreadyExists() {
        // Setup a different phone number for the update
        validPayload.setPhoneNumber("9999999999");

        when(intervieweeRepository.findById(validId)).thenReturn(Optional.of(mockInterviewee));
        when(intervieweeRepository.existsByEmail(validPayload.getEmail())).thenReturn(false);
        when(intervieweeRepository.existsByPhoneNumber(validPayload.getPhoneNumber())).thenReturn(true);

        InvalidArgumentException exception = assertThrows(InvalidArgumentException.class,
                () -> intervieweeService.updateInterviewee(validId, validPayload, validImage, validResume));

        assertEquals("Phone number already exists", exception.getMessage());
        verify(intervieweeRepository, never()).save(any(Interviewee.class));
    }

    @Test
    void updateInterviewee_ShouldThrowNotFoundException_WhenSchoolNotFound() {
        when(intervieweeRepository.findById(validId)).thenReturn(Optional.of(mockInterviewee));
        when(schoolService.getSchool(validPayload.getSchoolId())).thenReturn(null);

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> intervieweeService.updateInterviewee(validId, validPayload, validImage, validResume));

        assertEquals("School not found", exception.getMessage());
        verify(intervieweeRepository, never()).save(any(Interviewee.class));
    }

    @Test
    void updateInterviewee_ShouldNotCheckEmailUniqueness_WhenEmailUnchanged() {
        // Same email as the existing interviewee
        validPayload.setEmail("test@example.com");

        when(intervieweeRepository.findById(validId)).thenReturn(Optional.of(mockInterviewee));
        when(intervieweeRepository.save(any(Interviewee.class))).thenReturn(mockInterviewee);

        Interviewee result = intervieweeService.updateInterviewee(validId, validPayload, validImage, validResume);

        assertNotNull(result);
        assertEquals(mockInterviewee, result);
        verify(intervieweeRepository, times(1)).save(any(Interviewee.class));
        verify(intervieweeRepository, never()).existsByEmail(anyString());
    }

    @Test
    void updateInterviewee_ShouldNotCheckPhoneNumberUniqueness_WhenPhoneNumberUnchanged() {
        // Same phone number as the existing interviewee
        validPayload.setPhoneNumber("1234567890");

        when(intervieweeRepository.findById(validId)).thenReturn(Optional.of(mockInterviewee));
        when(intervieweeRepository.save(any(Interviewee.class))).thenReturn(mockInterviewee);

        Interviewee result = intervieweeService.updateInterviewee(validId, validPayload, validImage, validResume);

        assertNotNull(result);
        assertEquals(mockInterviewee, result);
        verify(intervieweeRepository, times(1)).save(any(Interviewee.class));
        verify(intervieweeRepository, never()).existsByPhoneNumber(anyString());
    }

    @Test
    void updateInterviewee_ShouldThrowFileException_WhenImageSizeExceedsLimit() {
        when(intervieweeRepository.findById(validId)).thenReturn(Optional.of(mockInterviewee));

        MultipartFile oversizedImage = new MockMultipartFile("image", "image.jpg", "image/jpeg", new byte[6 * 1024 * 1024]);

        FileException exception = assertThrows(FileException.class,
                () -> intervieweeService.updateInterviewee(validId, validPayload, oversizedImage, validResume));

        assertEquals("Image size exceeds the maximum allowed size of 5MB.", exception.getMessage());
        verify(intervieweeRepository, never()).save(any(Interviewee.class));
    }

    @Test
    void updateInterviewee_ShouldThrowFileException_WhenImageTypeIsInvalid() {
        when(intervieweeRepository.findById(validId)).thenReturn(Optional.of(mockInterviewee));

        MultipartFile invalidImageType = new MockMultipartFile("image", "image.gif", "image/gif", new byte[1024]);

        FileException exception = assertThrows(FileException.class,
                () -> intervieweeService.updateInterviewee(validId, validPayload, invalidImageType, validResume));

        assertEquals("Only JPEG and PNG images are allowed.", exception.getMessage());
        verify(intervieweeRepository, never()).save(any(Interviewee.class));
    }

    @Test
    void updateInterviewee_ShouldThrowFileException_WhenResumeSizeExceedsLimit() {
        when(intervieweeRepository.findById(validId)).thenReturn(Optional.of(mockInterviewee));

        MultipartFile oversizedResume = new MockMultipartFile("resume", "resume.pdf", "application/pdf", new byte[11 * 1024 * 1024]);

        FileException exception = assertThrows(FileException.class,
                () -> intervieweeService.updateInterviewee(validId, validPayload, validImage, oversizedResume));

        assertEquals("Resume size exceeds the maximum allowed size of 10MB.", exception.getMessage());
        verify(intervieweeRepository, never()).save(any(Interviewee.class));
    }

    @Test
    void updateInterviewee_ShouldThrowFileException_WhenResumeTypeIsInvalid() {
        when(intervieweeRepository.findById(validId)).thenReturn(Optional.of(mockInterviewee));

        MultipartFile invalidResumeType = new MockMultipartFile("resume", "resume.doc", "application/msword", new byte[1024]);

        FileException exception = assertThrows(FileException.class,
                () -> intervieweeService.updateInterviewee(validId, validPayload, validImage, invalidResumeType));

        assertEquals("Only PDF files are allowed.", exception.getMessage());
        verify(intervieweeRepository, never()).save(any(Interviewee.class));
    }

    @Test
    void updateInterviewee_ShouldHandleIoException_WhenProcessingFilesFails() throws IOException {
        when(intervieweeRepository.findById(validId)).thenReturn(Optional.of(mockInterviewee));

        // Mock IntervieweePayload to throw IOException when toEntity is called
        IntervieweePayload mockPayload = mock(IntervieweePayload.class);
        when(mockPayload.getEmail()).thenReturn("test@example.com");
        when(mockPayload.getPhoneNumber()).thenReturn("1234567890");
        when(mockPayload.getSchoolId()).thenReturn(validPayload.getSchoolId());
        when(mockPayload.toEntity(any(Interviewee.class), any(MultipartFile.class), any(MultipartFile.class), any(School.class)))
                .thenThrow(new IOException("Test IO exception"));

        FileException exception = assertThrows(FileException.class,
                () -> intervieweeService.updateInterviewee(validId, mockPayload, validImage, validResume));

        assertTrue(exception.getMessage().contains("Failed to process the image or resume"));
        verify(intervieweeRepository, never()).save(any(Interviewee.class));
    }

    @Test
    void deleteInterviewee_ShouldDeleteInterviewee_WhenFound() {
        when(intervieweeRepository.findById(validId)).thenReturn(Optional.of(mockInterviewee));
        doNothing().when(intervieweeRepository).delete(mockInterviewee);

        intervieweeService.deleteInterviewee(validId);

        verify(intervieweeRepository, times(1)).delete(mockInterviewee);
    }

    @Test
    void deleteInterviewee_ShouldThrowNotFoundException_WhenNotFound() {
        when(intervieweeRepository.findById(validId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> intervieweeService.deleteInterviewee(validId));

        assertEquals("Interviewee not found", exception.getMessage());
        verify(intervieweeRepository, never()).delete(any(Interviewee.class));
    }

    @Test
    void saveInterviewee_ShouldHandleIoException_WhenProcessingFilesFails() throws IOException {
        when(intervieweeRepository.existsByEmail(validPayload.getEmail())).thenReturn(false);
        when(intervieweeRepository.existsByPhoneNumber(validPayload.getPhoneNumber())).thenReturn(false);

        // Mock IntervieweePayload to throw IOException when toEntity is called
        IntervieweePayload mockPayload = mock(IntervieweePayload.class);
        when(mockPayload.getEmail()).thenReturn("test@example.com");
        when(mockPayload.getPhoneNumber()).thenReturn("1234567890");
        when(mockPayload.getSchoolId()).thenReturn(validPayload.getSchoolId());
        when(mockPayload.toEntity(any(MultipartFile.class), any(MultipartFile.class), any(School.class)))
                .thenThrow(new IOException("Test IO exception"));

        FileException exception = assertThrows(FileException.class,
                () -> intervieweeService.saveInterviewee(mockPayload, validImage, validResume));

        assertTrue(exception.getMessage().contains("Failed to process the image or resume"));
        verify(intervieweeRepository, never()).save(any(Interviewee.class));
    }

    @Test
    void getIntervieweeByPhone_ShouldReturnFalse_WhenPhoneNumberBelongsToExcludedId() {
        String phoneNumber = "1234567890";
        UUID excludeId = validId;

        when(intervieweeRepository.findById(excludeId)).thenReturn(Optional.of(mockInterviewee));

        Boolean result = intervieweeService.getIntervieweeByPhone(phoneNumber, excludeId);

        assertFalse(result);
        verify(intervieweeRepository, times(1)).findById(excludeId);
        verify(intervieweeRepository, never()).existsByPhoneNumber(phoneNumber);
    }

    @Test
    void getIntervieweeByPhone_ShouldThrowNotFoundException_WhenExcludedIntervieweeNotFound() {
        String phoneNumber = "1234567890";
        UUID excludeId = UUID.randomUUID();

        when(intervieweeRepository.findById(excludeId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> intervieweeService.getIntervieweeByPhone(phoneNumber, excludeId));
        verify(intervieweeRepository, times(1)).findById(excludeId);
        verify(intervieweeRepository, never()).existsByPhoneNumber(phoneNumber);
    }

    @Test
    void getIntervieweeByEmail_ShouldReturnFalse_WhenEmailBelongsToExcludedId() {
        String email = "test@example.com";
        UUID excludeId = validId;

        when(intervieweeRepository.findById(excludeId)).thenReturn(Optional.of(mockInterviewee));

        Boolean result = intervieweeService.getIntervieweeByEmail(email, excludeId);

        assertFalse(result);
        verify(intervieweeRepository, times(1)).findById(excludeId);
        verify(intervieweeRepository, never()).existsByEmail(email);
    }

    @Test
    void getIntervieweeByEmail_ShouldThrowNotFoundException_WhenExcludedIntervieweeNotFound() {
        String email = "test@example.com";
        UUID excludeId = UUID.randomUUID();

        when(intervieweeRepository.findById(excludeId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> intervieweeService.getIntervieweeByEmail(email, excludeId));
        verify(intervieweeRepository, times(1)).findById(excludeId);
        verify(intervieweeRepository, never()).existsByEmail(email);
    }

    @Test
    void getIntervieweeByPhone_ShouldReturnTrue_WhenPhoneNumberExists() {
        String phoneNumber = "1234567890";

        // Test with null excludeId
        when(intervieweeRepository.existsByPhoneNumber(phoneNumber)).thenReturn(true);
        Boolean result = intervieweeService.getIntervieweeByPhone(phoneNumber, null);
        assertTrue(result);
        verify(intervieweeRepository, times(1)).existsByPhoneNumber(phoneNumber);
    }

    @Test
    void getIntervieweeByPhone_ShouldReturnFalse_WhenPhoneNumberDoesNotExist() {
        String phoneNumber = "1234567890";

        // Test with null excludeId
        when(intervieweeRepository.existsByPhoneNumber(phoneNumber)).thenReturn(false);
        Boolean result = intervieweeService.getIntervieweeByPhone(phoneNumber, null);
        assertFalse(result);
        verify(intervieweeRepository, times(1)).existsByPhoneNumber(phoneNumber);
    }

    @Test
    void getIntervieweeByEmail_ShouldReturnTrue_WhenEmailExists() {
        String email = "test@example.com";

        // Test with null excludeId
        when(intervieweeRepository.existsByEmail(email)).thenReturn(true);
        Boolean result = intervieweeService.getIntervieweeByEmail(email, null);
        assertTrue(result);
        verify(intervieweeRepository, times(1)).existsByEmail(email);
    }

    @Test
    void getIntervieweeByEmail_ShouldReturnFalse_WhenEmailDoesNotExist() {
        String email = "test@example.com";

        // Test with null excludeId
        when(intervieweeRepository.existsByEmail(email)).thenReturn(false);
        Boolean result = intervieweeService.getIntervieweeByEmail(email, null);
        assertFalse(result);
        verify(intervieweeRepository, times(1)).existsByEmail(email);
    }
}