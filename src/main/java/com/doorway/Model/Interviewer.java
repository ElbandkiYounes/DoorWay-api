package com.doorway.Model;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(columnDefinition = "BYTEA")
    @Builder.Default
    private byte[] profilePicture = new byte[0];
}
