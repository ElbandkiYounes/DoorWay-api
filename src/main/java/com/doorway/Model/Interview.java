package com.doorway.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "interviews")
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID Id;
    private String feedback;
    @Builder.Default
    private Decision decision = Decision.NEUTRAL;

    private Date scheduledAt;

    @ManyToOne
    private Interviewee interviewee;

    @ManyToOne
    private InterviewingProcess interviewingProcess;

    @OneToMany
    @Builder.Default
    private List<PrincipleAnswer> principleAnswers = Collections.emptyList();

    @OneToMany
    @Builder.Default
    private List<TechnicalAnswer> technicalAnswers  = Collections.emptyList();

}
