package ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private String transactionId;
    private String fromAccountNumber;
    private String toAccountNumber;
    private String transactionType; // DEPOSIT, WITHDRAW, TRANSFER
    private double amount;
    private LocalDateTime timestamp;
    private String description;
    private String fromCustomerName;
    private String toCustomerName;
    
    // Constructor for deposit/withdraw
    public Transaction(String transactionId, String accountNumber, String customerName, 
                      String transactionType, double amount, String description) {
        this.transactionId = transactionId;
        this.fromAccountNumber = accountNumber;
        this.fromCustomerName = customerName;
        this.transactionType = transactionType;
        this.amount = amount;
        this.description = description;
        this.timestamp = LocalDateTime.now();
        this.toAccountNumber = null;
        this.toCustomerName = null;
    }
    
    // Constructor for transfer
    public Transaction(String transactionId, String fromAccountNumber, String fromCustomerName,
                      String toAccountNumber, String toCustomerName, double amount, String description) {
        this.transactionId = transactionId;
        this.fromAccountNumber = fromAccountNumber;
        this.fromCustomerName = fromCustomerName;
        this.toAccountNumber = toAccountNumber;
        this.toCustomerName = toCustomerName;
        this.transactionType = "TRANSFER";
        this.amount = amount;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters
    public String getTransactionId() { return transactionId; }
    public String getFromAccountNumber() { return fromAccountNumber; }
    public String getToAccountNumber() { return toAccountNumber; }
    public String getTransactionType() { return transactionType; }
    public double getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getDescription() { return description; }
    public String getFromCustomerName() { return fromCustomerName; }
    public String getToCustomerName() { return toCustomerName; }
    
    public String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return timestamp.format(formatter);
    }
    
    public String getFormattedAmount() {
        return String.format("$%.2f", amount);
    }
    
    @Override
    public String toString() {
        return String.format("%s - %s: %s", getFormattedTimestamp(), transactionType, getFormattedAmount());
    }
} 
