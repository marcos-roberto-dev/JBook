package com.rocket.study.library;

import com.rocket.study.book.Book;
import com.rocket.study.member.Member;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class LibraryTransaction {
    private Member member;
    private Book book;
    private int quantity;
    private LocalDateTime borrowDate;
    private LocalDateTime returnDate;
    private LocalDateTime expectedReturnDate;
    private LocalDateTime actualReturnDate;
    private double totalPrice;

    public LibraryTransaction(Member member, Book book, int quantity, LocalDateTime expectedReturnDate) {
        this.member = member;
        this.book = book;
        this.quantity = quantity;
        this.borrowDate = LocalDateTime.now();
        this.expectedReturnDate = expectedReturnDate;
        this.totalPrice = calculateTotalPrice();
    }


    public double getTotalPrice() {
        return totalPrice;
    }

    public LocalDateTime getBorrowDate() {
        return borrowDate;
    }

    public LocalDateTime getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public LocalDateTime getActualReturnDate() {
        return actualReturnDate;
    }

    public Member getMember() {
        return member;
    }

    public Book getBook() {
        return book;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setActualReturnDate(LocalDateTime actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
        this.totalPrice = calculateFinalPrice();
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
        this.totalPrice = calculateTotalPrice();
    }

    private BigDecimal calculateInitialPrice() {

        return book.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    private double calculateFinalPrice() {
        BigDecimal basePrice = calculateInitialPrice();
        if (actualReturnDate != null && actualReturnDate.isAfter(expectedReturnDate)) {
            long daysLate = ChronoUnit.DAYS.between(expectedReturnDate, actualReturnDate);
            double lateFee = daysLate * (basePrice.doubleValue() * 0.01); // 1% de juros por dia de atraso
            return basePrice.doubleValue() + lateFee;
        }
        return basePrice.doubleValue();
    }

    private double calculateTotalPrice() {
        BigDecimal dailyRate = book.getPrice().multiply(BigDecimal.valueOf(quantity));
        long daysBorrowed = borrowDate.until(
                returnDate != null ? returnDate : LocalDateTime.now(),
                ChronoUnit.DAYS
        );
        return dailyRate.multiply(BigDecimal.valueOf(daysBorrowed)).doubleValue();
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "member=" + member.getName() +
                ", book=" + book.getTitle() +
                ", quantity=" + quantity +
                ", borrowDate=" + borrowDate +
                ", expectedReturnDate=" + expectedReturnDate +
                ", actualReturnDate=" + actualReturnDate +
                ", totalPrice=" + totalPrice +
                '}';
    }

}
