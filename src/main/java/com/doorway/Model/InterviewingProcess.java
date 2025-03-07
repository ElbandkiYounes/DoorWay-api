package com.doorway.Model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "interviewing_processes")
public class InterviewingProcess {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Builder.Default
    private Decision decision = Decision.NEUTRAL;
    private String feedback;

    @ManyToOne
    private Role role;

    @ManyToOne
    private Interviewee interviewee;

    @OneToMany
    @Builder.Default
    private List<Interview> interviews = Collections.emptyList();

    @CreatedDate
    private Date createdAt;
}
