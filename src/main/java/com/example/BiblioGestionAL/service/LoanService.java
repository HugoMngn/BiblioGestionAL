package com.example.BiblioGestionAL.service;

import com.example.BiblioGestionAL.entity.Book;
import com.example.BiblioGestionAL.entity.Loan;
import com.example.BiblioGestionAL.entity.User;
import com.example.BiblioGestionAL.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
        // Mark book as temporarily unavailable to prevent race
        book.setAvailable(false);
        bookService.update(book);
        Loan saved = repo.save(loan);
        // notify librarians (we store notifications; actual notify logic in NotificationService)
        notificationService.notifyLibrarians("New loan request for book: " + book.getTitle());
        return saved;
    }

    public Loan approveLoan(Long loanId, User approver) {
        Optional<Loan> opt = repo.findById(loanId);
        if (opt.isEmpty()) throw new IllegalArgumentException("Loan not found");
        Loan loan = opt.get();
        loan.setApproved(true);
        repo.save(loan);
        notificationService.notifyUser(loan.getUser(), "Your loan for '" + loan.getBook().getTitle() + "' has been approved.");
        return loan;
    }

    public Loan returnBook(Long loanId) {
        Loan loan = repo.findById(loanId).orElseThrow(() -> new IllegalArgumentException("Loan not found"));
        loan.setReturnDate(LocalDate.now());
        loan.getBook().setAvailable(true);
        bookService.update(loan.getBook());
        repo.save(loan);
        notificationService.notifyUser(loan.getUser(), "Book '" + loan.getBook().getTitle() + "' returned. Thank you!");
        return loan;
    }

    public List<Loan> getUserLoans(User user) { return repo.findByUser(user); }

    public List<Loan> getPendingLoans() { return repo.findByApprovedFalse(); }
}
