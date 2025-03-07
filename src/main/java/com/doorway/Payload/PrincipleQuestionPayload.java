package com.doorway.Payload;

import com.doorway.Model.ExcellencePrinciple;
import com.doorway.Model.PrincipleQuestion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PrincipleQuestionPayload {
    @NotBlank(message = "Question is mandatory")
    private String question;

    @NotNull(message = "Principle is mandatory")
    private ExcellencePrinciple principle;

    public PrincipleQuestion toEntity() {
        return PrincipleQuestion.builder()
                .question(question)
                .principle(principle)
                .build();
    }

    public PrincipleQuestion toEntity(PrincipleQuestion principleQuestion) {
        principleQuestion.setQuestion(question);
        principleQuestion.setPrinciple(principle);
        return principleQuestion;
    }
}
