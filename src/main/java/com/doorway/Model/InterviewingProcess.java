package com.doorway.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.*;
@EntityListeners(AuditingEntityListener.class)
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
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Decision decision = Decision.NEUTRAL;
    @Column(columnDefinition = "TEXT")
    private String feedback;


    @ManyToOne
    private Role role;

    @ManyToOne
    @JsonIgnore
    private Interviewee interviewee;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnore
    private List<Interview> interviews = Collections.emptyList();

    @CreatedDate
    private LocalDateTime createdAt;

    @JsonProperty("intervieweeId")
    public UUID getIntervieweeId() {
        return interviewee.getId();
    }
}
