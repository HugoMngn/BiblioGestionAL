package com.example.BiblioGestionAL.service;

import com.example.BiblioGestionAL.entity.Book;
import com.example.BiblioGestionAL.entity.Loan;
import com.example.BiblioGestionAL.entity.User;
import com.example.BiblioGestionAL.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService {

    private final LoanRepository repo;
    private final BookService bookService;
    private final NotificationService notificationService;

    @Autowired
    public LoanService(LoanRepository repo, BookService bookService, NotificationService notificationService) {
        this.repo = repo;
        this.bookService = bookService;
        this.notificationService = notificationService;
    }

    // 🔹 Demande d’emprunt
    public Loan requestLoan(User user, Book book) {
        if (!book.isAvailable()) {
            throw new IllegalStateException("Book not available");
        }

        Loan loan = Loan.builder()
                .user(user)
                .book(book)
                .loanDate(LocalDate.now())
                .dueDate(LocalDate.now().plusWeeks(2))
                .approved(false)
                .build();

        // Marque le livre comme temporairement indisponible
        book.setAvailable(false);
        bookService.update(book);

        Loan saved = repo.save(loan);
        notificationService.notifyLibrarians("New loan request for book: " + book.getTitle());
        return saved;
    }

    // 🔹 Validation d’un emprunt
    public Loan approveLoan(Long loanId, User approver) {
        Loan loan = repo.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        loan.setApproved(true);
        repo.save(loan);

        notificationService.notifyUser(
                loan.getUser(),
                "Your loan for '" + loan.getBook().getTitle() + "' has been approved."
        );

        return loan;
    }

    // 🔹 Retour d’un livre
    public Loan returnBook(Long loanId) {
        Loan loan = repo.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        loan.setReturnDate(LocalDate.now());
        loan.getBook().setAvailable(true);

        bookService.update(loan.getBook());
        repo.save(loan);

        notificationService.notifyUser(
                loan.getUser(),
                "Book '" + loan.getBook().getTitle() + "' returned. Thank you!"
        );

        return loan;
    }

    // 🔹 Emprunts d’un utilisateur
    public List<Loan> getUserLoans(User user) {
        return repo.findByUser(user);
    }

    // 🔹 Emprunts en attente de validation
    public List<Loan> getPendingLoans() {
        return repo.findByApprovedFalse();
    }

    // 🔹 Emprunts en retard
    public List<Loan> findOverdueLoans() {
        LocalDate today = LocalDate.now();
        return repo.findByDueDateBefore(today);
    }

    // 🔹 Emprunts actifs (non rendus)
    public List<Loan> findActiveLoans() {
        return repo.findByReturnDateIsNull();
    }

    // 🔹 Emprunts terminés (rendus)
    public List<Loan> findCompletedLoans() {
        return repo.findByReturnDateIsNotNull();
    }

    // 🔹 Trouver les emprunts non approuvés
    public List<Loan> findByApprovedFalse() {
        return repo.findByApprovedFalse();
    }

    // 🔹 Trouver tous les emprunts
    public List<Loan> findAll() {
        return repo.findAll();
    }
}
