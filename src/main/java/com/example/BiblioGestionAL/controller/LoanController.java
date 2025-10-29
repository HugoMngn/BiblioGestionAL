package com.example.BiblioGestionAL.controller;

import com.example.BiblioGestionAL.entity.Loan;
import com.example.BiblioGestionAL.facade.LibraryFacadeProxy;
import com.example.BiblioGestionAL.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    private final LibraryFacadeProxy facade;
    private final LoanService loanService; // Ajoutez ceci

    @Autowired
    public LoanController(LibraryFacadeProxy facade, LoanService loanService) { 
        this.facade = facade;
        this.loanService = loanService;
    }

    @PostMapping("/request")
    public ResponseEntity<?> requestLoan(@RequestParam String username, @RequestParam Long bookId) {
        try {
            Loan loan = facade.requestLoan(username, bookId);
            return ResponseEntity.ok(loan);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/approve")
    public ResponseEntity<?> approve(@RequestParam Long loanId, @RequestParam String approver) {
        try {
            Loan l = facade.approveLoan(loanId, approver);
            return ResponseEntity.ok(l);
        } catch (SecurityException se) {
            return ResponseEntity.status(403).body(se.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/return")
    public ResponseEntity<?> returnBook(@RequestParam Long loanId, @RequestParam String username) {
        try {
            Loan l = facade.returnBook(loanId, username);
            return ResponseEntity.ok(l);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/my")
    public ResponseEntity<?> myLoans(@RequestParam String username) {
        return ResponseEntity.ok(facade.getUserLoans(username));
    }

    @GetMapping("/pending")
    public ResponseEntity<?> pendingLoans(@RequestParam String username) {
        try {
            // Retourne TOUS les emprunts non approuvés (approved = false)
            List<Loan> pending = loanService.findByApprovedFalse();
            return ResponseEntity.ok(pending);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // NOUVEAU: Endpoint pour tous les emprunts (pour admin)
    @GetMapping("/all")
    public ResponseEntity<?> allLoans() {
        try {
            List<Loan> allLoans = loanService.findAll();
            return ResponseEntity.ok(allLoans);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}