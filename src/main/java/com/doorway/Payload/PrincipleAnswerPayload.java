package com.doorway.Payload;

import com.doorway.Model.Bar;
import com.doorway.Model.Interview;
import com.doorway.Model.PrincipleAnswer;
import com.doorway.Model.PrincipleQuestion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class PrincipleAnswerPayload {

    @NotBlank(message = "Answer cannot be blank")
    private String answer;

@NotNull(message = "Bar cannot be null")
    private Bar bar;



    public PrincipleAnswer toEntity(PrincipleQuestion question, Interview interview) {
        return PrincipleAnswer.builder()
                .answer(answer)
                .bar(bar)
                .question(question)
                .interview(interview)
                .build();
    }

    public PrincipleAnswer toEntity(PrincipleAnswer principleAnswer) {
        principleAnswer.setAnswer(answer);
        principleAnswer.setBar(bar);
        return principleAnswer;
    }
}
