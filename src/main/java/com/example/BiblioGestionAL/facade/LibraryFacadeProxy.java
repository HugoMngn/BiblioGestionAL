package com.example.BiblioGestionAL.facade;

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

    @Autowired
    public LibraryFacadeProxy(LibraryFacadeImpl real, UserService userService) {
        this.real = real;
        this.userService = userService;
    }

    private User requireUser(String username) {
        return userService.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private boolean hasRole(User u, Role role) {
        return u.getRoles().contains(role);
    }

    @Override
    public User registerMember(String username, String password, String fullName) {
        // open to all
        return real.registerMember(username, password, fullName);
    }

    @Override
    public User findUserByUsername(String username) {
        return real.findUserByUsername(username);
    }

    @Override
    public com.example.BiblioGestionAL.entity.Book addBook(com.example.BiblioGestionAL.entity.Book b) {
        // only librarians/admins should be allowed - for proxy we require a "system" check; here we assume admin created via service
        // For demo, allow direct add (controller should restrict)
        return real.addBook(b);
    }

    @Override
    public List<com.example.BiblioGestionAL.entity.Book> searchBooksByTitle(String title) { return real.searchBooksByTitle(title); }

    @Override
    public List<com.example.BiblioGestionAL.entity.Book> availableBooks() { return real.availableBooks(); }

    @Override
    public com.example.BiblioGestionAL.entity.Loan requestLoan(String username, Long bookId) {
        User u = requireUser(username);
        // any member can request
        if (!hasRole(u, Role.ROLE_MEMBER) && !hasRole(u, Role.ROLE_LIBRARIAN) && !hasRole(u, Role.ROLE_ADMIN)) {
            throw new SecurityException("Not allowed to request loans");
        }
        return real.requestLoan(username, bookId);
    }

    @Override
    public com.example.BiblioGestionAL.entity.Loan approveLoan(Long loanId, String approverUsername) {
        User approver = requireUser(approverUsername);
        if (!hasRole(approver, Role.ROLE_LIBRARIAN) && !hasRole(approver, Role.ROLE_ADMIN)) {
            throw new SecurityException("Only librarians or admins can approve loans");
        }
        return real.approveLoan(loanId, approverUsername);
    }

    @Override
    public com.example.BiblioGestionAL.entity.Loan returnBook(Long loanId, String username) {
        return real.returnBook(loanId, username);
    }

    @Override
    public List<com.example.BiblioGestionAL.entity.Loan> getUserLoans(String username) {
        return real.getUserLoans(username);
    }
}
