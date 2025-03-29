package com.doorway.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "interviewees")
public class Interviewee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    private String email;
    private Date dateOfBirth;
    private String phoneNumber;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private School school;

    @OneToMany(mappedBy = "interviewee", fetch = FetchType.LAZY)
    @Builder.Default
    @JsonIgnore
    private List<InterviewingProcess> interviewingProcesses = new ArrayList<>();

    @Column(columnDefinition = "BYTEA")
    @Builder.Default
    private byte[] profilePicture = new byte[0];

    @Column(columnDefinition = "BYTEA")
    @Builder.Default
    private byte[] resume = new byte[0];

    @JsonProperty("newestInterviewingProcess")
    public InterviewingProcess getNewestInterviewingProcess() {
        return interviewingProcesses.stream()
                .max(Comparator.comparing(InterviewingProcess::getCreatedAt))
                .orElse(null);
    }
}