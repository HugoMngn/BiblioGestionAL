package com.example.BiblioGestionAL.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "loan")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Loan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Book book;
    private String username;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    @Builder.Default
    private boolean approved = false;
}
