package com.doorway.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "technical_answers",
        uniqueConstraints = @UniqueConstraint(columnNames = {"question_id", "interview_id"}))
public class TechnicalAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Language language;
    private String answer;
    private Bar bar;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private TechnicalQuestion question;

    @ManyToOne
    @JoinColumn(name = "interview_id", nullable = false)
    private Interview interview;
}
