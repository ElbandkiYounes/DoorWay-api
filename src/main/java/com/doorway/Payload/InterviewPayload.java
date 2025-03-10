package com.doorway.Payload;

import com.doorway.Model.Decision;
import com.doorway.Model.Interview;
import com.doorway.Model.Interviewer;
import com.doorway.Model.InterviewingProcess;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewPayload {

    private String feedback;

    @Builder.Default
    private Decision decision = Decision.NEUTRAL;

    @NotNull(message = "Schedule date is mandatory")
    @Future(message = "The interview must be scheduled in the future")
    private LocalDateTime scheduledAt;

    @NotNull(message = "Interviewer ID is required")
    private UUID interviewerId;

    // For creating a new interview
    public Interview toEntity(Interviewer interviewer, InterviewingProcess interviewingProcess) {
        return Interview.builder()
                .feedback(feedback)
                .decision(decision)
                .scheduledAt(scheduledAt)
                .interviewer(interviewer)
                .interviewingProcess(interviewingProcess)
                .build();
    }

    // For updating an existing interview
    public Interview toEntity(Interview interview, Interviewer interviewer) {
            interview.setFeedback(feedback);
            interview.setDecision(decision);
        interview.setScheduledAt(scheduledAt);
        interview.setInterviewer(interviewer);
        return interview;
    }


}