package com.demo_ai.demo_ai;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name ="ai_responses")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AI {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int userId;
    private String request;
    @Lob
    @Column(columnDefinition = "CLOB")
    private String response;
}
