package com.aluracursos.literalura.main;
import com.aluracursos.literalura.models.Author;
import com.aluracursos.literalura.models.BookDetails
import com.aluracursos.literalura.models.Book;
import com.aluracursos.literalura.repository.AuthorRepository;
import com.aluracursos.literalura.repository.BookRepository;
import com.aluracursos.literalura.services.DataConverter;
import com.aluracursos.literalura.services.RequestAPI;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private RequestAPI requestAPI = new RequestAPI();
    private Scanner scanner = new Scanner(System.in);
    private DataConverter dataConverter = new DataConverter();
    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private String urlBase ="https://gutendex.com/books/";
    private List<Book> books;
    private List<Author> autores;

    public Main(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    // Mostrar el menu en consola
    public void Menu()
    {
        var option = -1;
        while (option != 0){
            var menu ="""
                    **************************************************
                        LiterAlura - Busqueda de Libros y Autores
                          Elija la opción a traves de su número 
                    **************************************************
                    
                    Selecciona una opcion a continuación: 
                    
                    1. Buscar libro por titulo
                    2. Listar libros registrados
                    3. Listar autores registrados
                    4. Lista autores vivos en un determinado año
                    5. Lista libros por idioma
                    0. Salir               
                    """;

            try {
                System.out.println(menu);
                option = scanner.nextInt();
                scanner.nextLine();
            }catch (Exception e){

                System.out.println("Ingresa una opcion valida, por favor.");
            }

            switch (option){
                case 1:
                    FindBook();
                    break;
                case 2:
                    QueryBook();
                    break;
                case 3:
                    QueryAuthors();
                    break;
                case 4:
                    QueryAuthorsByYear();
                    break;
                case 5:
                    QueryBookByLanguage();
                    break;
                case 0:
                    System.out.println("Hasta luego");
                    break;
                default:
                    System.out.println("Ingresa una opcion valida");
            }
        }
    }

    // Extrae los datos de un libro
    private BookDetails getBookDetails() {
        System.out.println("Ingrese el nombre del libro");
        var busqueda = scanner.nextLine().toLowerCase().replace(" ","%20");
        var json = requestAPI.getData(urlBase + "?search=" + busqueda);

        BookDetails bookDetails = convierteDatos.obtenerDatos(json, BookDetails.class);
        return bookDetails;
    }

// Busca un libro y guarda infromacion en la BD en sus tablas correspondientes
    private void FindBook()
    {
        BookDetails bookDetails = getBookDetails();

        try {
            Book book = new Book(bookDetails.resultados().get(0));
            Author author = new Author(bookDetails.resultados().get(0).autorList().get(0));

            System.out.println("""
                    libro[
                        titulo: %s
                        author: %s
                        lenguaje: %s
                        descargas: %s
                    ]
                    """.formatted(book.getTitulo(),
                    book.getAutor(),
                    book.getLenguaje(),
                    book.getDescargas().toString()));

            bookRepository.save(book);
            authorRepository.save(author);

        }catch (Exception e){
            System.out.println("no se encontro ese libro");
        }

    }

    // Trae los libros guardados en la BD
    private void QueryBook() {
        books = bookRepository.findAll();
        books.stream().forEach(l -> {
            System.out.println("""    
                        Titulo: %s
                        Author: %s
                        Lenguaje: %s
                        Descargas: %s
                    """.formatted(l.getTitulo(),
                    l.getAutor(),
                    l.getLenguaje(),
                    l.getDescargas().toString()));
        });
    }

    // Trae todos los autores de los libros consultados en la BD
    private void QueryAuthors() {
        autores = authorRepository.findAll();
        autores.stream().forEach(a -> {
            System.out.println("""
                        Autor: %s
                        Año de nacimiento: %s
                        Año de defuncion: %s
                    """.formatted(a.getAutor(),
                    a.getNacimiento().toString(),
                    a.getDefuncion().toString()));
        });
    }

    // Trae a los autores apartir de cierto año
    public void QueryAuthorsByYear()
    {
        System.out.println("Ingresa el año a partir del cual buscar:");
        var anoBusqueda = scanner.nextInt();
        scanner.nextLine();

        List<Author> authors = authorRepository.autorPorFecha(anoBusqueda);
        authors.forEach( a -> {
            System.out.println("""
                    Nombre: %s
                    Fecha de nacimiento: %s
                    Fecha de defuncion: %s
                    """.formatted(a.getAutor(),a.getNacimiento().toString(),a.getDefuncion().toString()));
        });
    }


    private void QueryBookByLanguage()
    {
        System.out.println("""
                ****************************************************************    
                    Selecciona el lenguaje de los libros que deseas consultar
                ****************************************************************
                1 - En (Ingles)
                2 - Es (Español)
                """);

        try {

            var opcion2 = scanner.nextInt();
            scanner.nextLine();

            switch (opcion2) {
                case 1:
                    books = bookRepository.findByLenguaje("en");
                    break;
                case 2:
                    books = bookRepository.findByLenguaje("es");
                    break;

                default:
                    System.out.println("Ingresa una opcion valida");
            }

            books.stream().forEach(l -> {
                System.out.println("""    
                        Titulo: %s
                        Author: %s
                        Lenguaje: %s
                        Descargas: %s
                    """.formatted(l.getTitulo(),
                        l.getAutor(),
                        l.getLenguaje(),
                        l.getDescargas().toString()));
            });

        } catch (Exception e){
            System.out.println("Ingresa un valor valido");
        }
    }
}
