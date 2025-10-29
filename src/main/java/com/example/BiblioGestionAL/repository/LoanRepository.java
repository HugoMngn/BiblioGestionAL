package com.example.BiblioGestionAL.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BiblioGestionAL.entity.Loan;
import com.example.BiblioGestionAL.entity.User;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByUser(User user);
    List<Loan> findByApprovedFalse();
}
