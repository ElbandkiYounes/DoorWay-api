package com.doorway.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "interviewees")
public class Interviewee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    private String email;
    private Date dateOfBirth;
    private String phoneNumber;
    private String password;
    @ManyToOne
    private School school;

    @OneToMany
    @Builder.Default
    @JsonIgnore
    private List<InterviewingProcess> interviewingProcesses = Collections.emptyList();

    @Column(columnDefinition = "BYTEA")
    @Builder.Default
    private byte[] profilePicture = new byte[0];

    @Column(columnDefinition = "BYTEA")
    @Builder.Default
    private byte[] resume = new byte[0];
}

