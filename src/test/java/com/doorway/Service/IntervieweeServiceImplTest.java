package com.doorway.Service;

import com.doorway.Exception.FileException;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getIntervieweeById_ShouldReturnInterviewee_WhenFound() {
        UUID id = UUID.randomUUID();
        Interviewee interviewee = new Interviewee();
        when(intervieweeRepository.findById(id)).thenReturn(Optional.of(interviewee));

        Interviewee result = intervieweeService.getIntervieweeById(id);

        assertNotNull(result);
        assertEquals(interviewee, result);
        verify(intervieweeRepository, times(1)).findById(id);
    }

    @Test
    void getIntervieweeById_ShouldThrowNotFoundException_WhenNotFound() {
        UUID id = UUID.randomUUID();
        when(intervieweeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> intervieweeService.getIntervieweeById(id));
        verify(intervieweeRepository, times(1)).findById(id);
    }

    @Test
    void getAllInterviewees_ShouldReturnAllInterviewees() {
        List<Interviewee> interviewees = List.of(new Interviewee(), new Interviewee());
        when(intervieweeRepository.findAll()).thenReturn(interviewees);

        List<Interviewee> result = intervieweeService.getAllInterviewees(null);

        assertNotNull(result);
        assertEquals(interviewees, result);
        verify(intervieweeRepository, times(1)).findAll();
    }

    @Test
    void getAllInterviewees_ShouldReturnIntervieweesByDecision() {
        Decision decision = Decision.NEUTRAL;
        List<Interviewee> interviewees = List.of(new Interviewee(), new Interviewee());
        when(intervieweeRepository.findAllByInterviewingProcesses_Decision(decision)).thenReturn(interviewees);

        List<Interviewee> result = intervieweeService.getAllInterviewees(decision);

        assertNotNull(result);
        assertEquals(interviewees, result);
        verify(intervieweeRepository, times(1)).findAllByInterviewingProcesses_Decision(decision);
    }

    @Test
    void saveInterviewee_ShouldSaveInterviewee_WhenValid() {
        IntervieweePayload payload = new IntervieweePayload();
        MultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", new byte[0]);
        MultipartFile resume = new MockMultipartFile("resume", "resume.pdf", "application/pdf", new byte[0]);
        School school = new School();
        when(schoolService.getSchool(payload.getSchoolId())).thenReturn(school);
        Interviewee interviewee = new Interviewee();
        when(intervieweeRepository.save(any(Interviewee.class))).thenReturn(interviewee);

        Interviewee result = intervieweeService.saveInterviewee(payload, image, resume);

        assertNotNull(result);
        assertEquals(interviewee, result);
        verify(intervieweeRepository, times(1)).save(any(Interviewee.class));
    }

    @Test
    void saveInterviewee_ShouldThrowFileException_WhenImageSizeExceedsLimit() {
        IntervieweePayload payload = new IntervieweePayload();
        MultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", new byte[6 * 1024 * 1024]);
        MultipartFile resume = new MockMultipartFile("resume", "resume.pdf", "application/pdf", new byte[0]);

        assertThrows(FileException.class, () -> intervieweeService.saveInterviewee(payload, image, resume));
    }

    @Test
    void updateInterviewee_ShouldUpdateInterviewee_WhenValid() {
        UUID id = UUID.randomUUID();
        IntervieweePayload payload = new IntervieweePayload();
        MultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", new byte[0]);
        MultipartFile resume = new MockMultipartFile("resume", "resume.pdf", "application/pdf", new byte[0]);
        Interviewee interviewee = new Interviewee();
        School school = new School();
        when(intervieweeRepository.findById(id)).thenReturn(Optional.of(interviewee));
        when(schoolService.getSchool(payload.getSchoolId())).thenReturn(school);
        when(intervieweeRepository.save(any(Interviewee.class))).thenReturn(interviewee);

        Interviewee result = intervieweeService.updateInterviewee(id, payload, image, resume);

        assertNotNull(result);
        assertEquals(interviewee, result);
        verify(intervieweeRepository, times(1)).save(any(Interviewee.class));
    }

    @Test
    void deleteInterviewee_ShouldDeleteInterviewee_WhenFound() {
        UUID id = UUID.randomUUID();
        Interviewee interviewee = new Interviewee();
        when(intervieweeRepository.findById(id)).thenReturn(Optional.of(interviewee));
        doNothing().when(intervieweeRepository).delete(interviewee);

        intervieweeService.deleteInterviewee(id);

        verify(intervieweeRepository, times(1)).delete(interviewee);
    }

    @Test
    void deleteInterviewee_ShouldThrowNotFoundException_WhenNotFound() {
        UUID id = UUID.randomUUID();
        when(intervieweeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> intervieweeService.deleteInterviewee(id));
        verify(intervieweeRepository, times(1)).findById(id);
    }
}