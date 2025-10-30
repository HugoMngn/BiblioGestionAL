package com.example.BiblioGestionAL.dto;

import com.example.BiblioGestionAL.entity.Loan;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class LoanDTO {
    // Loan Data Transfer Object
    private Long id;
    private String username;
    private Long bookId;
    private String bookTitle;
    private String bookAuthor;
    private String bookGenre;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private Boolean approved;

    // Convert Loan entity to LoanDTO
    public static LoanDTO fromEntity(Loan loan) {
        return LoanDTO.builder()
                .id(loan.getId())
                .username(loan.getUser() != null ? loan.getUser().getUsername() : null)
                .bookId(loan.getBook() != null ? loan.getBook().getId() : null)
                .bookTitle(loan.getBook() != null ? loan.getBook().getTitle() : null)
                .bookAuthor(loan.getBook() != null ? loan.getBook().getAuthor() : null)
                .bookGenre(loan.getBook() != null ? loan.getBook().getGenre() : null)
                .loanDate(loan.getLoanDate())
                .dueDate(loan.getDueDate())
                .returnDate(loan.getReturnDate())
                .approved(loan.getApproved())
                .build();
    }
}