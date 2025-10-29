package com.example.BiblioGestionAL.facade;

import com.example.BiblioGestionAL.entity.Book;
import com.example.BiblioGestionAL.entity.Loan;
import com.example.BiblioGestionAL.entity.User;
import com.example.BiblioGestionAL.repository.BookRepository;
import com.example.BiblioGestionAL.repository.UserRepository;
import com.example.BiblioGestionAL.service.BookService;
import com.example.BiblioGestionAL.service.LoanService;
import com.example.BiblioGestionAL.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LibraryFacadeImpl implements LibraryFacade {

    private final UserService userService;
    private final BookService bookService;
    private final LoanService loanService;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Autowired
    public LibraryFacadeImpl(UserService userService, BookService bookService,
                             LoanService loanService, UserRepository userRepository,
                             BookRepository bookRepository) {
        this.userService = userService;
        this.bookService = bookService;
        this.loanService = loanService;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public User registerMember(String username, String password, String fullName) {
        return userService.registerMember(username, password, fullName);
    }

    @Override
    public User findUserByUsername(String username) {
        return userService.findByUsername(username).orElse(null);
    }

    @Override
    public Book addBook(Book b) { return bookService.addBook(b); }

    @Override
    public List<Book> searchBooksByTitle(String title) { return bookService.searchByTitle(title); }

    @Override
    public List<Book> availableBooks() { return bookService.availableBooks(); }

    @Override
    public Loan requestLoan(String username, Long bookId) {
        User user = userService.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new IllegalArgumentException("Book not found"));
        return loanService.requestLoan(user, book);
    }

    @Override
    public Loan approveLoan(Long loanId, String approverUsername) {
        User approver = userService.findByUsername(approverUsername).orElseThrow(() -> new IllegalArgumentException("Approver not found"));
        return loanService.approveLoan(loanId, approver);
    }

    @Override
    public Loan returnBook(Long loanId, String username) {
        return loanService.returnBook(loanId);
    }

    @Override
    public List<Loan> getUserLoans(String username) {
        User u = userService.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return loanService.getUserLoans(u);
    }
}
