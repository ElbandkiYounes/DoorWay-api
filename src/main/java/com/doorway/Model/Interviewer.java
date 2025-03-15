package com.doorway.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "profilePicture")
@Table(name = "interviewers")
public class Interviewer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    private String email;

    private String phoneNumber;

    private String password;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @JsonIgnore
    @OneToMany(mappedBy = "interviewer")
    private List<Interview> interviews = Collections.emptyList();


    @Column(columnDefinition = "BYTEA")
    @Builder.Default
    private byte[] profilePicture = new byte[0];
}
