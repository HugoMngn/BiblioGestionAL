package com.example.BiblioGestionAL.facade;

import com.example.BiblioGestionAL.entity.Book;
import com.example.BiblioGestionAL.entity.Loan;
import com.example.BiblioGestionAL.entity.Role;
import com.example.BiblioGestionAL.entity.User;
import com.example.BiblioGestionAL.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Proxy to check authorizations before delegating to the real facade.
 */
@Component
public class LibraryFacadeProxy implements LibraryFacade {

    private final LibraryFacadeImpl real;
    private final UserService userService;

    // Constructor with Dependency Injection
    @Autowired
    public LibraryFacadeProxy(LibraryFacadeImpl real, UserService userService) {
        this.real = real;
        this.userService = userService;
    }

    // Helper Methods
    private User requireUser(String username) {
        return userService.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    // Check if user has a specific role
    private boolean hasRole(User u, Role role) {
        return u.getRoles().contains(role);
    }

    // Implementation of Facade Methods with Authorization Checks
    @Override
    public User registerMember(String username, String password, String fullName) {
        return real.registerMember(username, password, fullName);
    }

    // User Lookup
    @Override
    public User findUserByUsername(String username) {
        return real.findUserByUsername(username);
    }

    // Book Management
    @Override
    public Book addBook(Book b) {
        return real.addBook(b);
    }

    // Book Search by Title
    @Override
    public List<Book> searchBooksByTitle(String title) {
        return real.searchBooksByTitle(title);
    }

    // Available Books Listing
    @Override
    public List<Book> availableBooks() {
        return real.availableBooks();
    }

    // Loan Management with Authorization Checks
    @Override
    public Loan requestLoan(String username, Long bookId) {
        User user = requireUser(username);
        if (!hasRole(user, Role.ROLE_MEMBER) && 
            !hasRole(user, Role.ROLE_LIBRARIAN) && 
            !hasRole(user, Role.ROLE_ADMIN)) {
            throw new SecurityException("Not allowed to request loans");
        }
        return real.requestLoan(username, bookId);
    }

    // Approve Loan Request
    @Override
    public Loan approveLoan(Long loanId, String approverUsername) {
        User approver = requireUser(approverUsername);
        if (!hasRole(approver, Role.ROLE_LIBRARIAN) &&
                !hasRole(approver, Role.ROLE_ADMIN)) {
            throw new SecurityException("Only librarians or admins can approve loans");
        }
        return real.approveLoan(loanId, approverUsername);
    }

    // Return Book
    @Override
    public Loan returnBook(Long loanId, String username) {
        return real.returnBook(loanId, username);
    }

    // Get Loans of a User
    @Override
    public List<Loan> getUserLoans(String username) {
        return real.getUserLoans(username);
    }

    // Get Pending Loans (for librarians/admins)
    @Override
    public List<Loan> getPendingLoans(String username) {
        User u = requireUser(username);
        if (!hasRole(u, Role.ROLE_LIBRARIAN) && 
            !hasRole(u, Role.ROLE_ADMIN)) {
            throw new SecurityException("Only librarians or admins can view pending loans");
        }
        return real.getPendingLoans(username);
    }
}