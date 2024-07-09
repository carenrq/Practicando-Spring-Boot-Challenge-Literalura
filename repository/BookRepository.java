package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookRepository extends JpaRepository<Book,Long>
{

    @Query("SELECT l FROM Libro l WHERE l.lenguaje ILIKE %:lenguaje%")
    List<Book> FindByLanguage(String lenguaje);
}
