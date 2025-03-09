package com.doorway.Service;

import com.doorway.Exception.NotFoundException;
import com.doorway.Model.Decision;
import com.doorway.Model.Interviewee;
import com.doorway.Model.InterviewingProcess;
import com.doorway.Model.Role;
import com.doorway.Payload.InterviewingProcessPayload;
import com.doorway.Repository.InterviewingProcessRepository;
import com.doorway.Service.Interface.IntervieweeService;
import com.doorway.Service.Interface.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InterviewingProcessServiceImplTest {

    @Mock
    private InterviewingProcessRepository interviewingProcessRepository;

    @Mock
    private RoleService roleService;

    @Mock
    private IntervieweeService intervieweeService;

    @InjectMocks
    private InterviewingProcessServiceImpl interviewingProcessService;

    private UUID interviewingProcessId;
    private UUID intervieweeId;
    private Long roleId;
    private InterviewingProcess interviewingProcess;
    private InterviewingProcessPayload interviewingProcessPayload;
    private Role role;
    private Interviewee interviewee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        interviewingProcessId = UUID.randomUUID();
        intervieweeId = UUID.randomUUID();
        roleId = 1L;

        // Initialize test data
        role = new Role();
        role.setId(roleId);

        interviewee = new Interviewee();
        interviewee.setId(intervieweeId);

        interviewingProcess = InterviewingProcess.builder()
                .id(interviewingProcessId)
                .decision(Decision.NEUTRAL)
                .feedback("Initial feedback")
                .role(role)
                .interviewee(interviewee)
                .build();

        interviewingProcessPayload = InterviewingProcessPayload.builder()
                .decision(Decision.INCLINED)
                .feedback("Test feedback")
                .roleId(roleId)
                .build();
    }

    @Test
    void getInterviewingProcessById_ShouldReturnProcess_WhenFound() {
        when(interviewingProcessRepository.findById(interviewingProcessId)).thenReturn(Optional.of(interviewingProcess));

        InterviewingProcess result = interviewingProcessService.getInterviewingProcessById(interviewingProcessId);

        assertNotNull(result);
        assertEquals(interviewingProcess, result);
        verify(interviewingProcessRepository, times(1)).findById(interviewingProcessId);
    }

    @Test
    void getInterviewingProcessById_ShouldThrowNotFoundException_WhenNotFound() {
        when(interviewingProcessRepository.findById(interviewingProcessId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> interviewingProcessService.getInterviewingProcessById(interviewingProcessId));
        verify(interviewingProcessRepository, times(1)).findById(interviewingProcessId);
    }

    @Test
    void createInterviewingProcess_ShouldCreateProcess_WhenValid() {
        when(roleService.getRole(roleId)).thenReturn(role);
        when(intervieweeService.getIntervieweeById(intervieweeId)).thenReturn(interviewee);
        when(interviewingProcessRepository.save(any(InterviewingProcess.class))).thenReturn(interviewingProcess);

        InterviewingProcess result = interviewingProcessService.createInterviewingProcess(intervieweeId, interviewingProcessPayload);

        assertNotNull(result);
        assertEquals(interviewingProcess, result);
        verify(roleService, times(1)).getRole(roleId);
        verify(intervieweeService, times(1)).getIntervieweeById(intervieweeId);
    }

    @Test
    void createInterviewingProcess_ShouldThrowNotFoundException_WhenRoleNotFound() {
        when(roleService.getRole(roleId)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> interviewingProcessService.createInterviewingProcess(intervieweeId, interviewingProcessPayload));
        verify(roleService, times(1)).getRole(roleId);
        verify(intervieweeService, never()).getIntervieweeById(any());
    }

    @Test
    void createInterviewingProcess_ShouldThrowNotFoundException_WhenIntervieweeNotFound() {
        when(roleService.getRole(roleId)).thenReturn(role);
        when(intervieweeService.getIntervieweeById(intervieweeId)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> interviewingProcessService.createInterviewingProcess(intervieweeId, interviewingProcessPayload));
        verify(roleService, times(1)).getRole(roleId);
        verify(intervieweeService, times(1)).getIntervieweeById(intervieweeId);
    }

    @Test
    void updateInterviewingProcess_ShouldUpdateProcess_WhenValid() {
        when(roleService.getRole(roleId)).thenReturn(role);
        when(intervieweeService.getIntervieweeById(intervieweeId)).thenReturn(interviewee);
        when(interviewingProcessRepository.findById(interviewingProcessId)).thenReturn(Optional.of(interviewingProcess));
        when(interviewingProcessRepository.save(any(InterviewingProcess.class))).thenReturn(interviewingProcess);

        InterviewingProcess result = interviewingProcessService.updateInterviewingProcess(interviewingProcessId, interviewingProcessPayload);

        assertNotNull(result);
        assertEquals(interviewingProcess, result);
        verify(roleService, times(1)).getRole(roleId);
        verify(interviewingProcessRepository, times(1)).findById(interviewingProcessId);
        verify(interviewingProcessRepository, times(1)).save(any(InterviewingProcess.class));
    }

    @Test
    void updateInterviewingProcess_ShouldThrowNotFoundException_WhenRoleNotFound() {
        when(roleService.getRole(roleId)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> interviewingProcessService.updateInterviewingProcess(interviewingProcessId, interviewingProcessPayload));
        verify(roleService, times(1)).getRole(roleId);
        verify(intervieweeService, never()).getIntervieweeById(any());
    }

    @Test
    void updateInterviewingProcess_ShouldThrowNotFoundException_WhenProcessNotFound() {
        when(roleService.getRole(roleId)).thenReturn(role);
        when(interviewingProcessRepository.findById(interviewingProcessId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> interviewingProcessService.updateInterviewingProcess(interviewingProcessId, interviewingProcessPayload));
        verify(roleService, times(1)).getRole(roleId);
        verify(interviewingProcessRepository, times(1)).findById(interviewingProcessId);
    }

    @Test
    void deleteInterviewingProcess_ShouldDeleteProcess_WhenFound() {
        when(interviewingProcessRepository.findById(interviewingProcessId)).thenReturn(Optional.of(interviewingProcess));
        doNothing().when(interviewingProcessRepository).deleteById(interviewingProcessId);

        interviewingProcessService.deleteInterviewingProcess(interviewingProcessId);

        verify(interviewingProcessRepository, times(1)).findById(interviewingProcessId);
        verify(interviewingProcessRepository, times(1)).deleteById(interviewingProcessId);
    }

    @Test
    void deleteInterviewingProcess_ShouldThrowNotFoundException_WhenNotFound() {
        when(interviewingProcessRepository.findById(interviewingProcessId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> interviewingProcessService.deleteInterviewingProcess(interviewingProcessId));
        verify(interviewingProcessRepository, times(1)).findById(interviewingProcessId);
        verify(interviewingProcessRepository, never()).deleteById(any());
    }

    @Test
    void getAllInterviewingProcessesByIntervieweeId_ShouldReturnProcesses_WhenIntervieweeExists() {
        List<InterviewingProcess> processes = List.of(interviewingProcess, interviewingProcess);
        when(intervieweeService.getIntervieweeById(intervieweeId)).thenReturn(interviewee);
        when(interviewingProcessRepository.findAllByIntervieweeId(intervieweeId)).thenReturn(processes);

        List<InterviewingProcess> result = interviewingProcessService.getAllInterviewingProcessesByIntervieweeId(intervieweeId);

        assertNotNull(result);
        assertEquals(processes, result);
        assertEquals(2, result.size());
        verify(intervieweeService, times(1)).getIntervieweeById(intervieweeId);
        verify(interviewingProcessRepository, times(1)).findAllByIntervieweeId(intervieweeId);
    }

    @Test
    void getAllInterviewingProcessesByIntervieweeId_ShouldThrowNotFoundException_WhenIntervieweeNotFound() {
        when(intervieweeService.getIntervieweeById(intervieweeId)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> interviewingProcessService.getAllInterviewingProcessesByIntervieweeId(intervieweeId));
        verify(intervieweeService, times(1)).getIntervieweeById(intervieweeId);
        verify(interviewingProcessRepository, never()).findAllByIntervieweeId(any());
    }
}