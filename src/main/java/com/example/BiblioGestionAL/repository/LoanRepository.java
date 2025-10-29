package com.example.BiblioGestionAL.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BiblioGestionAL.entity.Loan;
import com.example.BiblioGestionAL.entity.User;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByUser(User user);
    List<Loan> findByApprovedFalse();

    List<Loan> findByReturnedTrue();

    List<Loan> findByReturnedFalse();
    List<Loan> findByDueDateBefore(LocalDate date);
}
