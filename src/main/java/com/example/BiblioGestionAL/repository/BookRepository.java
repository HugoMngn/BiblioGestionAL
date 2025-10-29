package com.example.BiblioGestionAL.repository;

import com.example.BiblioGestionAL.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByAuthorContainingIgnoreCase(String author);
    List<Book> findByGenreContainingIgnoreCase(String genre);

    @Query("select b from Book b where b.available = true")
    List<Book> findAvailableBooks();
}
