package com.doorway.Service;

import com.doorway.Exception.ConflictException;
import com.doorway.Exception.NotFoundException;
import com.doorway.Model.Interview;
import com.doorway.Model.Interviewee;
import com.doorway.Model.Interviewer;
import com.doorway.Model.InterviewingProcess;
import com.doorway.Payload.InterviewPayload;
import com.doorway.Repository.InterviewRepository;
import com.doorway.Service.Interface.IntervieweeService;
import com.doorway.Service.Interface.InterviewerService;
import com.doorway.Service.Interface.InterviewingProcessService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class InterviewServiceImplTest {

    @Mock
    private InterviewRepository interviewRepository;

    @Mock
    private InterviewerService interviewerService;

    @Mock
    private IntervieweeService intervieweeService;

    @Mock
    private InterviewingProcessService interviewingProcessService;

    @InjectMocks
    private InterviewServiceImpl interviewService;

    private UUID interviewId;
    private UUID intervieweeId;
    private UUID processId;
    private UUID interviewerId;
    private InterviewPayload interviewPayload;
    private Interview interview;
    private Interviewee interviewee;
    private Interviewer interviewer;
    private InterviewingProcess interviewingProcess;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        interviewId = UUID.randomUUID();
        intervieweeId = UUID.randomUUID();
        processId = UUID.randomUUID();
        interviewerId = UUID.randomUUID();

        interviewPayload = new InterviewPayload();
        interviewPayload.setInterviewerId(interviewerId);

        interview = new Interview();
        interviewee = new Interviewee();
        interviewee.setId(intervieweeId);

        interviewer = new Interviewer();
        interviewer.setId(interviewerId);

        interviewingProcess = new InterviewingProcess();
        interviewingProcess.setId(processId);
        interviewingProcess.setInterviewee(interviewee);
    }

    @Test
    void getInterviewById_ShouldReturnInterview_WhenFound() {
        when(interviewRepository.findById(interviewId)).thenReturn(Optional.of(interview));

        Interview result = interviewService.getInterviewById(interviewId);

        assertNotNull(result);
        assertEquals(interview, result);
        verify(interviewRepository, times(1)).findById(interviewId);
    }

    @Test
    void getInterviewById_ShouldThrowNotFoundException_WhenNotFound() {
        when(interviewRepository.findById(interviewId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> interviewService.getInterviewById(interviewId));
        verify(interviewRepository, times(1)).findById(interviewId);
    }

    @Test
    void getInterviewsByProcess_ShouldReturnInterviews() {
        List<Interview> interviews = List.of(new Interview(), new Interview());
        when(interviewRepository.findAllByInterviewingProcessId(processId)).thenReturn(interviews);

        List<Interview> result = interviewService.getInterviewsByProcess(intervieweeId, processId);

        assertNotNull(result);
        assertEquals(interviews, result);
        verify(interviewRepository, times(1)).findAllByInterviewingProcessId(processId);
    }

    @Test
    void getInterviewsByInterviewer_ShouldReturnInterviews() {
        List<Interview> interviews = List.of(new Interview(), new Interview());
        when(interviewRepository.findAllByInterviewerId(interviewerId)).thenReturn(interviews);

        List<Interview> result = interviewService.getInterviewsByInterviewer(interviewerId);

        assertNotNull(result);
        assertEquals(interviews, result);
        verify(interviewRepository, times(1)).findAllByInterviewerId(interviewerId);
    }

    @Test
    void createInterview_ShouldCreateInterview_WhenValid() {
        // Setup mocks
        when(interviewingProcessService.getInterviewingProcessById(processId)).thenReturn(interviewingProcess);
        when(intervieweeService.getIntervieweeById(intervieweeId)).thenReturn(interviewee);
        when(interviewerService.getInterviewerById(interviewerId)).thenReturn(interviewer);
        when(interviewRepository.save(any(Interview.class))).thenReturn(interview);

        // Method call
        Interview result = interviewService.createInterview(intervieweeId, processId, interviewPayload);

        // Verify results
        assertNotNull(result);
        assertEquals(interview, result);
        verify(interviewingProcessService, times(1)).getInterviewingProcessById(processId);
        verify(intervieweeService, times(1)).getIntervieweeById(intervieweeId);
        verify(interviewerService, times(1)).getInterviewerById(interviewerId);
        verify(interviewRepository, times(1)).save(any(Interview.class));
    }

    @Test
    void createInterview_ShouldThrowNotFoundException_WhenProcessNotFound() {
        when(interviewingProcessService.getInterviewingProcessById(processId)).thenReturn(null);

        assertThrows(NotFoundException.class, () ->
                interviewService.createInterview(intervieweeId, processId, interviewPayload));

        verify(interviewingProcessService, times(1)).getInterviewingProcessById(processId);
        verify(intervieweeService, never()).getIntervieweeById(any());
        verify(interviewerService, never()).getInterviewerById(any());
        verify(interviewRepository, never()).save(any());
    }

    @Test
    void createInterview_ShouldThrowNotFoundException_WhenIntervieweeNotFound() {
        when(interviewingProcessService.getInterviewingProcessById(processId)).thenReturn(interviewingProcess);
        when(intervieweeService.getIntervieweeById(intervieweeId)).thenReturn(null);

        assertThrows(NotFoundException.class, () ->
                interviewService.createInterview(intervieweeId, processId, interviewPayload));

        verify(interviewingProcessService, times(1)).getInterviewingProcessById(processId);
        verify(intervieweeService, times(1)).getIntervieweeById(intervieweeId);
        verify(interviewerService, never()).getInterviewerById(any());
        verify(interviewRepository, never()).save(any());
    }

    @Test
    void createInterview_ShouldThrowConflictException_WhenIntervieweeNotInProcess() {
        Interviewee differentInterviewee = new Interviewee();
        differentInterviewee.setId(UUID.randomUUID());

        when(interviewingProcessService.getInterviewingProcessById(processId)).thenReturn(interviewingProcess);
        when(intervieweeService.getIntervieweeById(intervieweeId)).thenReturn(differentInterviewee);

        assertThrows(ConflictException.class, () ->
                interviewService.createInterview(intervieweeId, processId, interviewPayload));

        verify(interviewingProcessService, times(1)).getInterviewingProcessById(processId);
        verify(intervieweeService, times(1)).getIntervieweeById(intervieweeId);
        verify(interviewerService, never()).getInterviewerById(any());
        verify(interviewRepository, never()).save(any());
    }

    @Test
    void createInterview_ShouldThrowNotFoundException_WhenInterviewerNotFound() {
        when(interviewingProcessService.getInterviewingProcessById(processId)).thenReturn(interviewingProcess);
        when(intervieweeService.getIntervieweeById(intervieweeId)).thenReturn(interviewee);
        when(interviewerService.getInterviewerById(interviewerId)).thenReturn(null);

        assertThrows(NotFoundException.class, () ->
                interviewService.createInterview(intervieweeId, processId, interviewPayload));

        verify(interviewingProcessService, times(1)).getInterviewingProcessById(processId);
        verify(intervieweeService, times(1)).getIntervieweeById(intervieweeId);
        verify(interviewerService, times(1)).getInterviewerById(interviewerId);
        verify(interviewRepository, never()).save(any());
    }

    @Test
    void deleteInterview_ShouldDeleteInterview_WhenFound() {
        when(interviewRepository.findById(interviewId)).thenReturn(Optional.of(interview));
        doNothing().when(interviewRepository).deleteById(interviewId);

        interviewService.deleteInterview(interviewId);

        verify(interviewRepository, times(1)).findById(interviewId);
        verify(interviewRepository, times(1)).deleteById(interviewId);
    }

    @Test
    void deleteInterview_ShouldThrowNotFoundException_WhenNotFound() {
        when(interviewRepository.findById(interviewId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> interviewService.deleteInterview(interviewId));

        verify(interviewRepository, times(1)).findById(interviewId);
        verify(interviewRepository, never()).deleteById(any());
    }

    @Test
    void updateInterview_ShouldUpdateInterview_WhenValid() {
        when(interviewingProcessService.getInterviewingProcessById(processId)).thenReturn(interviewingProcess);
        when(intervieweeService.getIntervieweeById(intervieweeId)).thenReturn(interviewee);
        when(interviewerService.getInterviewerById(interviewerId)).thenReturn(interviewer);
        when(interviewRepository.findById(interviewId)).thenReturn(Optional.of(interview));
        when(interviewRepository.save(any(Interview.class))).thenReturn(interview);

        Interview result = interviewService.updateInterview(interviewId, intervieweeId, processId, interviewPayload);

        assertNotNull(result);
        assertEquals(interview, result);
        verify(interviewingProcessService, times(1)).getInterviewingProcessById(processId);
        verify(intervieweeService, times(1)).getIntervieweeById(intervieweeId);
        verify(interviewerService, times(1)).getInterviewerById(interviewerId);
        verify(interviewRepository, times(1)).findById(interviewId);
        verify(interviewRepository, times(1)).save(any(Interview.class));
    }

    @Test
    void updateInterview_ShouldThrowNotFoundException_WhenProcessNotFound() {
        when(interviewingProcessService.getInterviewingProcessById(processId)).thenReturn(null);

        assertThrows(NotFoundException.class, () ->
                interviewService.updateInterview(interviewId, intervieweeId, processId, interviewPayload));

        verify(interviewingProcessService, times(1)).getInterviewingProcessById(processId);
        verify(intervieweeService, never()).getIntervieweeById(any());
        verify(interviewerService, never()).getInterviewerById(any());
        verify(interviewRepository, never()).findById(any());
        verify(interviewRepository, never()).save(any());
    }

    @Test
    void updateInterview_ShouldThrowNotFoundException_WhenIntervieweeNotFound() {
        when(interviewingProcessService.getInterviewingProcessById(processId)).thenReturn(interviewingProcess);
        when(intervieweeService.getIntervieweeById(intervieweeId)).thenReturn(null);

        assertThrows(NotFoundException.class, () ->
                interviewService.updateInterview(interviewId, intervieweeId, processId, interviewPayload));

        verify(interviewingProcessService, times(1)).getInterviewingProcessById(processId);
        verify(intervieweeService, times(1)).getIntervieweeById(intervieweeId);
        verify(interviewerService, never()).getInterviewerById(any());
        verify(interviewRepository, never()).findById(any());
        verify(interviewRepository, never()).save(any());
    }

    @Test
    void updateInterview_ShouldThrowConflictException_WhenIntervieweeNotInProcess() {
        // Create a different interviewee than the one in the process
        Interviewee differentInterviewee = new Interviewee();
        UUID differentId = UUID.randomUUID();
        differentInterviewee.setId(differentId);

        when(interviewingProcessService.getInterviewingProcessById(processId)).thenReturn(interviewingProcess);
        when(intervieweeService.getIntervieweeById(intervieweeId)).thenReturn(differentInterviewee);

        assertThrows(ConflictException.class, () ->
                interviewService.updateInterview(interviewId, intervieweeId, processId, interviewPayload));

        verify(interviewingProcessService, times(1)).getInterviewingProcessById(processId);
        verify(intervieweeService, times(1)).getIntervieweeById(intervieweeId);
        verify(interviewerService, never()).getInterviewerById(any());
        verify(interviewRepository, never()).findById(any());
        verify(interviewRepository, never()).save(any());
    }

    @Test
    void updateInterview_ShouldThrowNotFoundException_WhenInterviewerNotFound() {
        when(interviewingProcessService.getInterviewingProcessById(processId)).thenReturn(interviewingProcess);
        when(intervieweeService.getIntervieweeById(intervieweeId)).thenReturn(interviewee);
        when(interviewerService.getInterviewerById(interviewerId)).thenReturn(null);

        assertThrows(NotFoundException.class, () ->
                interviewService.updateInterview(interviewId, intervieweeId, processId, interviewPayload));

        verify(interviewingProcessService, times(1)).getInterviewingProcessById(processId);
        verify(intervieweeService, times(1)).getIntervieweeById(intervieweeId);
        verify(interviewerService, times(1)).getInterviewerById(interviewerId);
        verify(interviewRepository, never()).findById(any());
        verify(interviewRepository, never()).save(any());
    }

    @Test
    void updateInterview_ShouldThrowNotFoundException_WhenInterviewNotFound() {
        when(interviewingProcessService.getInterviewingProcessById(processId)).thenReturn(interviewingProcess);
        when(intervieweeService.getIntervieweeById(intervieweeId)).thenReturn(interviewee);
        when(interviewerService.getInterviewerById(interviewerId)).thenReturn(interviewer);
        when(interviewRepository.findById(interviewId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                interviewService.updateInterview(interviewId, intervieweeId, processId, interviewPayload));

        verify(interviewingProcessService, times(1)).getInterviewingProcessById(processId);
        verify(intervieweeService, times(1)).getIntervieweeById(intervieweeId);
        verify(interviewerService, times(1)).getInterviewerById(interviewerId);
        verify(interviewRepository, times(1)).findById(interviewId);
        verify(interviewRepository, never()).save(any());
    }
}