package com.doorway.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "principle_answers")
public class PrincipleAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String answer;
    private Bar bar;

    @ManyToOne
    private PrincipleQuestion question;

    @ManyToOne
    private Interview interview;


}
