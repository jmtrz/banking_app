package ui;



public class Customer {
    private String customerId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private BankAccount account;
    
    public Customer(String customerId, String name, String email, String phone, String address, double initialBalance) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.account = new BankAccount(generateAccountNumber(), name, initialBalance);
    }
    
    private String generateAccountNumber() {
        return "ACC" + customerId;
    }
    
    // Getters and setters
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public BankAccount getAccount() { return account; }
    
    public String getAccountNumber() {
        return account.getAccountNumber();
    }
    
    public double getBalance() {
        return account.getBalance();
    }
    
    @Override
    public String toString() {
        return String.format("%s - %s (ID: %s)", name, getAccountNumber(), customerId);
    }
} 
