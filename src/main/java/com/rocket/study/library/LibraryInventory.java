package com.rocket.study.library;

import com.rocket.study.book.Book;
import com.rocket.study.member.Member;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class LibraryInventory {
    private Map<Book, Integer> inventory = new HashMap<>();
    private List<LibraryTransaction> borrowedBooksTransaction = new ArrayList<>();

    public void addBorrowedBookTransaction(Member member, Book book, int quantity, LocalDateTime returnDateBorrowed) {
        borrowedBooksTransaction.add(new LibraryTransaction(member, book, quantity, returnDateBorrowed));
    }

    public void listBorrowedBooksTransactions() {
        borrowedBooksTransaction.forEach(transaction -> {
            System.out.println("Livro emprestado para: " + transaction.getMember().getName());
            System.out.println(transaction.getBook() + ", quantidade emprestada: " + transaction.getQuantity());
            System.out.println("Data de empréstimo: " + transaction.getBorrowDate());
            System.out.println("Data de devolução: " + transaction.getExpectedReturnDate());
        });


    }

    public void returnBorrowedBookTransaction(Member member, Book book, int quantity) {
        System.out.println("Devolução realizada com sucesso para " + quantity + " exemplares do livro:" + book.getTitle());
        borrowedBooksTransaction.removeIf(transaction -> transaction.getMember().equals(member) && transaction.getBook().equals(book) && transaction.getQuantity() == quantity);


    }

    public void addBook(Book book, int quantity) {
        inventory.put(book, inventory.getOrDefault(book, 0) + quantity);
    }

    public void listAvailableBooks() {
        inventory.forEach((book, quantity) -> {
            if (quantity > 0) {
                System.out.println(book + ", quantidade disponível: " + quantity);
            }
        });
    }

    public boolean borrowBook(String title, int quantity, Member member, LocalDateTime returnDateBorrowed) {
        Optional<Book> bookToBorrow = inventory.keySet().stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(title))
                .findFirst();

        if (bookToBorrow.isPresent()) {
            Book book = bookToBorrow.get();
            int availableQuantity = inventory.get(book);

            if (availableQuantity >= quantity) {
                inventory.put(book, availableQuantity - quantity);
                System.out.println("Empréstimo realizado com sucesso para " + quantity + " exemplares do livro: " + title);
                addBorrowedBookTransaction(member, book, quantity, returnDateBorrowed);
                return true;
            } else {
                System.out.println("Quantidade insuficiente para o empréstimo do livro: " + title);
            }
        } else {
            System.out.println("Livro não encontrado no inventário: " + title);
        }

        return false;
    }

    public String returnBook(String title, int quantity, Member member) {
        Optional<Book> bookToReturn = inventory.keySet().stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(title))
                .findFirst();

        if (bookToReturn.isEmpty()) {
            System.out.println("Livro não encontrado no inventário.");
            return formatCurrency(BigDecimal.ZERO);
        }

        Book book = bookToReturn.get();
        BigDecimal totalToPay = BigDecimal.ZERO;
        int totalReturned = 0;

        System.out.println("\n-------------------\n");
        for (LibraryTransaction transaction : borrowedBooksTransaction) {
            if (transaction.getMember().equals(member) && transaction.getBook().equals(book)) {
                int transactionQuantity = transaction.getQuantity();

                if (totalReturned < quantity) {
                    int remainingToReturn = quantity - totalReturned;
                    int toReturn = Math.min(transactionQuantity, remainingToReturn);

                    totalToPay = totalToPay.add(book.getPrice().multiply(BigDecimal.valueOf(toReturn)));
                    totalReturned += toReturn;

                    if (totalReturned >= quantity) {
                        break;
                    }
                }
            }
        }

        String formattedTotalToPay = formatCurrency(totalToPay);

        System.out.println("Total a pagar por devolução: " + formattedTotalToPay);

        returnBorrowedBookTransaction(member, book, quantity);
        inventory.put(book, inventory.get(book) + quantity);

        return formattedTotalToPay;
    }


    private String formatCurrency(BigDecimal amount) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return currencyFormatter.format(amount);
    }


}