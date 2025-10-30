package com.example.BiblioGestionAL.controller;

import com.example.BiblioGestionAL.entity.Book;
import com.example.BiblioGestionAL.facade.LibraryFacadeProxy;
import com.example.BiblioGestionAL.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Endpoints pour gérer le catalogue
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final LibraryFacadeProxy facade;
    private final BookService bookService;

    // Constructor
    @Autowired
    public BookController(LibraryFacadeProxy facade, BookService bookService) {
        this.facade = facade;
        this.bookService = bookService;
    }

    // Add a new book
    @PostMapping
    public ResponseEntity<?> addBook(@RequestBody Book book,
            @RequestHeader(name = "X-User", required = false) String username) {
        // In real app: check role of username header; here facadeProxy + controller can enforce
        Book saved = facade.addBook(book);
        return ResponseEntity.ok(saved);
    }

    // Search books by title, author, or genre
    @GetMapping("/search")
    public ResponseEntity<List<Book>> search(@RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre) {
        if (title != null)
            return ResponseEntity.ok(bookService.searchByTitle(title));
        if (author != null)
            return ResponseEntity.ok(bookService.searchByAuthor(author));
        if (genre != null)
            return ResponseEntity.ok(bookService.searchByGenre(genre));
        return ResponseEntity.ok(bookService.availableBooks());
    }

    // Get book by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return bookService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Update book details
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        return bookService.findById(id)
                .map(existing -> {
                    existing.setTitle(updatedBook.getTitle());
                    existing.setAuthor(updatedBook.getAuthor());
                    existing.setGenre(updatedBook.getGenre());
                    existing.setAvailable(updatedBook.isAvailable());
                    Book saved = bookService.save(existing);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete a book
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        if (bookService.findById(id).isPresent()) {
            bookService.delete(id);
            return ResponseEntity.ok("Book deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}