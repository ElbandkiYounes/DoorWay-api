package com.doorway.Payload;

import com.doorway.Model.Decision;
import com.doorway.Model.Interviewee;
import com.doorway.Model.InterviewingProcess;
import com.doorway.Model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewingProcessPayload {

    @NotBlank(message = "Feedback is mandatory")
    private String feedback;

    @NotNull(message = "Decision is mandatory")
    private Decision decision;

    @NotNull(message = "Role is mandatory")
    private Long roleId;

    public InterviewingProcess toEntity(Role role, Interviewee interviewee) {
        return InterviewingProcess.builder()
                .feedback(feedback)
                .decision(decision)
                .role(role)
                .interviewee(interviewee)
                .build();
    }


    public InterviewingProcess toEntity(InterviewingProcess interviewingProcess, Role role) {
        interviewingProcess.setDecision(decision);
        interviewingProcess.setFeedback(feedback);
        interviewingProcess.setRole(role);
        return interviewingProcess;
    }
}
