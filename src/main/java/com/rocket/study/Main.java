package com.rocket.study;

import com.rocket.study.book.Book;
import com.rocket.study.library.LibraryInventory;
import com.rocket.study.member.Member;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        LibraryInventory libraryInventory = new LibraryInventory();

        Book book1 = new Book("1984", "George Orwell", BigDecimal.valueOf(5));
        Book book2 = new Book("Dom Quixote", "Miguel de Cervantes", BigDecimal.valueOf(8.5));
        Book book3 = new Book("O Senhor dos Anéis", "J.R.R. Tolkien", BigDecimal.valueOf(10));

        Member member1 = new Member("Marcos", "marcos@dev.com", "123456789");

        libraryInventory.addBook(book1, 3);
        libraryInventory.addBook(book2, 8);
        libraryInventory.addBook(book3, 5);

        libraryInventory.borrowBook("1984",2, member1, LocalDateTime.now().plusDays(5));
        libraryInventory.borrowBook("Dom Quixote",2, member1, LocalDateTime.now().plusDays(5));
        libraryInventory.borrowBook("O Senhor dos Anéis",4, member1, LocalDateTime.now().plusDays(5));

        System.out.println("\nLivros disponíveis após o empréstimo:");
        libraryInventory.listAvailableBooks();
        libraryInventory.listBorrowedBooksTransactions();

        libraryInventory.returnBook("1984", 2, member1);
        libraryInventory.returnBook("Dom Quixote", 2, member1);
        libraryInventory.returnBook("O Senhor dos Anéis", 4, member1);

        System.out.println("\nLivros disponíveis após a devolução:");
        libraryInventory.listAvailableBooks();
    }
}