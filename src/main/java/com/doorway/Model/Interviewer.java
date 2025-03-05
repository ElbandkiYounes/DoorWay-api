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
public class Interviewer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    private String email;

    private String phoneNumber;

    private String password;
    private String role;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] profilePicture;
}
