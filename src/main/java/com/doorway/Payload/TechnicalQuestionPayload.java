package com.doorway.Payload;

import com.doorway.Model.TechnicalQuestion;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
public class TechnicalQuestionPayload {
    @NotBlank(message = "Question is mandatory")
    private String question;

    public TechnicalQuestion toEntity() {
        return TechnicalQuestion.builder()
                .question(question)
                .build();
    }

    public TechnicalQuestion toEntity(TechnicalQuestion technicalQuestion) {
        technicalQuestion.setQuestion(question);
        return technicalQuestion;
    }
}
