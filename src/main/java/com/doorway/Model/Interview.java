package com.doorway.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "interviews")
@ToString
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String feedback;
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Decision decision = Decision.NEUTRAL;


    @Column(nullable = false)
    @Future(message = "The interview must be scheduled in the future")
    private LocalDateTime scheduledAt;


    @ManyToOne
    private Interviewer interviewer;

    @ManyToOne
    private InterviewingProcess interviewingProcess;

    @OneToMany
    @Builder.Default
    @JsonIgnore
    private List<PrincipleAnswer> principleAnswers = Collections.emptyList();

    @OneToMany
    @Builder.Default
    @JsonIgnore
    private List<TechnicalAnswer> technicalAnswers  = Collections.emptyList();

}
