package com.example.BiblioGestionAL.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BiblioGestionAL.entity.Loan;
import com.example.BiblioGestionAL.entity.User;

// Repository interface for Loan entity with custom query methods.
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByUser(User user);    
    List<Loan> findByApprovedFalse();
    List<Loan> findByDueDateBefore(LocalDate date);
    List<Loan> findByReturnDateIsNull();
    List<Loan> findByReturnDateIsNotNull();
}