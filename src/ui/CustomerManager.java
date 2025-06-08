package ui;

import java.util.ArrayList;
import java.util.List;

public class CustomerManager {
    private static CustomerManager instance;
    private List<Customer> customers;
    
    private CustomerManager() {
        customers = new ArrayList<>();
        loadSampleData();
    }
    
    public static CustomerManager getInstance() {
        if (instance == null) {
            instance = new CustomerManager();
        }
        return instance;
    }
    
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers);
    }
    
    public void addCustomer(Customer customer) {
        customers.add(customer);
    }
    
    public void removeCustomer(Customer customer) {
        customers.remove(customer);
    }
    
    public Customer findCustomerById(String customerId) {
        return customers.stream()
                .filter(c -> c.getCustomerId().equals(customerId))
                .findFirst()
                .orElse(null);
    }
    
    public Customer findCustomerByAccountNumber(String accountNumber) {
        return customers.stream()
                .filter(c -> c.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElse(null);
    }
    
    public boolean customerExists(String customerId) {
        return findCustomerById(customerId) != null;
    }
    
    public int getCustomerCount() {
        return customers.size();
    }
    
    private void loadSampleData() {
        customers.add(new Customer("CUST001", "John Doe", "john.doe@email.com", 
                                 "123-456-7890", "123 Main St, City, State", 1500.00));
        customers.add(new Customer("CUST002", "Jane Smith", "jane.smith@email.com", 
                                 "098-765-4321", "456 Oak Ave, City, State", 2500.00));
        customers.add(new Customer("CUST003", "Bob Johnson", "bob.johnson@email.com", 
                                 "555-123-4567", "789 Pine Rd, City, State", 750.00));
    }
} 
