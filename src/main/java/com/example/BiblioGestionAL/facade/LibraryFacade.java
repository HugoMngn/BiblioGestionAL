package com.example.BiblioGestionAL.facade;

import com.example.BiblioGestionAL.entity.Book;
import com.example.BiblioGestionAL.entity.Loan;
import com.example.BiblioGestionAL.entity.User;

import java.util.List;

public interface LibraryFacade {
    // Users
    User registerMember(String username, String password, String fullName);
    User findUserByUsername(String username);

    // Books
    Book addBook(Book b);
    List<Book> searchBooksByTitle(String title);
    List<Book> availableBooks();

    // Loans
    Loan requestLoan(String username, Long bookId);
    Loan approveLoan(Long loanId, String approverUsername);
    Loan returnBook(Long loanId, String username);

    List<Loan> getUserLoans(String username);
    List<Loan> getPendingLoans(String username);
}
