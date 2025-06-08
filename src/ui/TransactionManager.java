package ui;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionManager {
    private static TransactionManager instance;
    private List<Transaction> transactions;
    private int transactionCounter;
    
    private TransactionManager() {
        transactions = new ArrayList<>();
        transactionCounter = 1000; // Start with ID 1000
    }
    
    public static TransactionManager getInstance() {
        if (instance == null) {
            instance = new TransactionManager();
        }
        return instance;
    }
    
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }
    
    public String generateTransactionId() {
        return "TXN" + (++transactionCounter);
    }
    
    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }
    
    public List<Transaction> getTransactionsByCustomer(String customerName) {
        return transactions.stream()
                .filter(t -> customerName.equals(t.getFromCustomerName()) || 
                           customerName.equals(t.getToCustomerName()))
                .collect(Collectors.toList());
    }
    
    public List<Transaction> getTransactionsByAccountNumber(String accountNumber) {
        return transactions.stream()
                .filter(t -> accountNumber.equals(t.getFromAccountNumber()) || 
                           accountNumber.equals(t.getToAccountNumber()))
                .collect(Collectors.toList());
    }
    
    public List<Transaction> getTransactionsByType(String type) {
        return transactions.stream()
                .filter(t -> type.equals(t.getTransactionType()))
                .collect(Collectors.toList());
    }
    
    public int getTotalTransactionCount() {
        return transactions.size();
    }
    
    public double getTotalAmountByType(String type) {
        return transactions.stream()
                .filter(t -> type.equals(t.getTransactionType()))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }
    
    public int getTransactionCountByType(String type) {
        return (int) transactions.stream()
                .filter(t -> type.equals(t.getTransactionType()))
                .count();
    }
} 
