package com.doorway.Model;

import jakarta.persistence.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "technical_anwsers")
public class TechnicalAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Language language;
    private String answer;
    private Bar bar;

    @ManyToOne
    private TechnicalQuestion question;

    @ManyToOne
    private Interview interview;
}
