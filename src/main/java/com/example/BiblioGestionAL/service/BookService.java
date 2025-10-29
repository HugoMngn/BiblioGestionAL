package com.example.BiblioGestionAL.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BiblioGestionAL.entity.Book;
import com.example.BiblioGestionAL.repository.BookRepository;

@Service
public class BookService {
    private final BookRepository repo;
    @Autowired
    public BookService(BookRepository repo) { this.repo = repo; }

    public Book addBook(Book b) { return repo.save(b); }
    public Optional<Book> findById(Long id) { return repo.findById(id); }
    public List<Book> searchByTitle(String title) { return repo.findByTitleContainingIgnoreCase(title); }
    public List<Book> searchByAuthor(String author) { return repo.findByAuthorContainingIgnoreCase(author); }
    public List<Book> searchByGenre(String genre) { return repo.findByGenreContainingIgnoreCase(genre); }
    public List<Book> availableBooks() { return repo.findAvailableBooks(); }
    public Book update(Book b) { return repo.save(b); }
    public void delete(Long id) { repo.deleteById(id); }
}
