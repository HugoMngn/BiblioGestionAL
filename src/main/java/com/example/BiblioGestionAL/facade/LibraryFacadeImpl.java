package com.example.BiblioGestionAL.facade;

import com.example.BiblioGestionAL.entity.Book;
import com.example.BiblioGestionAL.entity.Loan;
import com.example.BiblioGestionAL.entity.Role;
import com.example.BiblioGestionAL.entity.User;
import com.example.BiblioGestionAL.repository.BookRepository;
import com.example.BiblioGestionAL.service.BookService;
import com.example.BiblioGestionAL.service.LoanService;
import com.example.BiblioGestionAL.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

// Implementation of the Library Facade
@Component
public class LibraryFacadeImpl implements LibraryFacade {

    // Services and Repositories
    private final UserService userService;
    private final BookService bookService;
    private final LoanService loanService;
    private final BookRepository bookRepository;

    // Constructor with Dependency Injection
    @Autowired
    public LibraryFacadeImpl(UserService userService, BookService bookService,
            LoanService loanService, BookRepository bookRepository) {
        this.userService = userService;
        this.bookService = bookService;
        this.loanService = loanService;
        this.bookRepository = bookRepository;
    }

    // Implementation of Facade Methods
    @Override
    public User registerMember(String username, String password, String fullName) {
        return userService.registerMember(username, password, fullName);
    }

    // User Lookup
    @Override
    public User findUserByUsername(String username) {
        return userService.findByUsername(username).orElse(null);
    }

    // Book Management
    @Override
    public Book addBook(Book b) {
        return bookService.addBook(b);
    }

    // Book Search by Title
    @Override
    public List<Book> searchBooksByTitle(String title) {
        return bookService.searchByTitle(title);
    }

    // Available Books Listing
    @Override
    public List<Book> availableBooks() {
        return bookService.availableBooks();
    }

    // Loan Management
    @Override
    public Loan requestLoan(String username, Long bookId) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new IllegalArgumentException("Book not found"));
        return loanService.requestLoan(user, book);
    }

    // Approve Loan Request
    @Override
    public Loan approveLoan(Long loanId, String approverUsername) {
        User approver = userService.findByUsername(approverUsername).orElseThrow(() -> new IllegalArgumentException("Approver not found"));
        return loanService.approveLoan(loanId, approver);
    }

    // Return Book
    @Override
    public Loan returnBook(Long loanId, String username) {
        return loanService.returnBook(loanId);
    }

    // Get Loans of a User
    @Override
    public List<Loan> getUserLoans(String username) {
        User u = userService.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return loanService.getUserLoans(u);
    }

    // Get Pending Loans (for librarians/admins)
    @Override
    public List<Loan> getPendingLoans(String username) {
        User u = userService.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!u.getRoles().contains(Role.ROLE_LIBRARIAN) && !u.getRoles().contains(Role.ROLE_ADMIN)) {
            throw new SecurityException("Only librarians or admins can view pending loans");
        }
        return loanService.getPendingLoans();
    }
}
