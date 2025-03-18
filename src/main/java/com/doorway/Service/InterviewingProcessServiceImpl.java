package com.doorway.Service;

import com.doorway.Exception.NotFoundException;
import com.doorway.Model.Interviewee;
import com.doorway.Model.InterviewingProcess;
import com.doorway.Model.Role;
import com.doorway.Payload.InterviewingProcessPayload;
import com.doorway.Repository.InterviewingProcessRepository;
import com.doorway.Service.Interface.IntervieweeService;
import com.doorway.Service.Interface.InterviewingProcessService;
import com.doorway.Service.Interface.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class InterviewingProcessServiceImpl implements InterviewingProcessService {
    InterviewingProcessRepository interviewingProcessRepository;
    RoleService roleService;
    IntervieweeService intervieweeService;

    @Autowired
    public InterviewingProcessServiceImpl(InterviewingProcessRepository interviewingProcessRepository, RoleService roleService, IntervieweeService intervieweeService) {
        this.interviewingProcessRepository = interviewingProcessRepository;
        this.roleService = roleService;
        this.intervieweeService = intervieweeService;
    }

    public List<InterviewingProcess> getAllInterviewingProcesses() {
        return interviewingProcessRepository.findAll();
    }

    @Override
    public InterviewingProcess getInterviewingProcessById(UUID interviewingProcessId) {
        return interviewingProcessRepository.findById(interviewingProcessId).orElseThrow(() -> new NotFoundException("Interviewing Process not found"));
    }

    @Override
    public InterviewingProcess createInterviewingProcess(UUID intervieweeId,InterviewingProcessPayload interviewingProcessPayload) {
        Role role = roleService.getRole(interviewingProcessPayload.getRoleId());
        if (role == null) {
            throw new NotFoundException("Role not found");
        }
        Interviewee interviewee = intervieweeService.getIntervieweeById(intervieweeId);
        if (interviewee == null) {
            throw new NotFoundException("Interviewee not found");
        }
        return interviewingProcessRepository.save(interviewingProcessPayload.toEntity(role, interviewee));

    }

    @Override
    public InterviewingProcess updateInterviewingProcess(UUID interviewingProcessId, InterviewingProcessPayload interviewingProcessPayload) {
        Role role = roleService.getRole(interviewingProcessPayload.getRoleId());
        if (role == null) {
            throw new NotFoundException("Role not found");
        }
        InterviewingProcess interviewingProcess = getInterviewingProcessById(interviewingProcessId);
        return interviewingProcessRepository.save(interviewingProcessPayload.toEntity(interviewingProcess, role));
    }

    @Override
    public void deleteInterviewingProcess(UUID interviewingProcessId) {
        getInterviewingProcessById(interviewingProcessId);
        interviewingProcessRepository.deleteById(interviewingProcessId);
    }


    @Override
    public List<InterviewingProcess> getAllInterviewingProcessesByIntervieweeId(UUID intervieweeId) {
        Interviewee interviewee = intervieweeService.getIntervieweeById(intervieweeId);
        if (interviewee == null) {
            throw new NotFoundException("Interviewee not found");
        }
        return interviewingProcessRepository.findAllByInterviewee_Id(intervieweeId);
    }


}
