package com.doorway.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "schools")
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "school")
    @Builder.Default
    private List<Interviewee> interviewees = Collections.emptyList();
}