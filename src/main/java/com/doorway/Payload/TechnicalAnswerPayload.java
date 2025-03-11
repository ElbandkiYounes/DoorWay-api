package com.doorway.Payload;

import com.doorway.Model.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class TechnicalAnswerPayload {

    @NotNull(message = "Language cannot be null")
    private Language language;

    @NotBlank(message = "Answer cannot be blank")
    @Size(max = 1000, message = "Answer cannot be longer than 1000 characters")
    private String answer;

    private Bar bar;



    public TechnicalAnswer toEntity(TechnicalQuestion question, Interview interview) {
        return TechnicalAnswer.builder()
                .answer(answer)
                .language(language)
                .interview(interview)
                .bar(bar)
                .question(question)
                .build();
    }

    public TechnicalAnswer toEntity(TechnicalAnswer technicalAnswer) {
        technicalAnswer.setLanguage(language);
        technicalAnswer.setAnswer(answer);
        technicalAnswer.setBar(bar);
        return technicalAnswer;
    }
}
