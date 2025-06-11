import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BankingApp {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainApp().setVisible(true);
            }
        });        
    }
}

class MainApp extends JFrame {
    
    // CardLayout and container panel
    private CardLayout cardLayout;
    private JPanel cardPanel;
    
    // The different screens/panels
    private Auth loginPanel;
    private Dashboard dashboardPanel;
    private CustomerManagement customerManagementPanel;
    private TransactionManagement transactionManagementPanel;
    private TransactionLogs transactionLogsPanel;
    
    public MainApp() {
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Banking Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 200);  // Initial size set for login panel
        setLocationRelativeTo(null);
        
        // Create the card layout and panel
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        // Create panels
        loginPanel = new Auth(this);
        dashboardPanel = new Dashboard(this);
        customerManagementPanel = new CustomerManagement(this);
        transactionManagementPanel = new TransactionManagement(this);
        transactionLogsPanel = new TransactionLogs(this);
        
        // Add panels to the card layout
        cardPanel.add(loginPanel, "login");
        cardPanel.add(dashboardPanel, "dashboard");
        cardPanel.add(customerManagementPanel, "customerManagement");
        cardPanel.add(transactionManagementPanel, "transactionManagement");
        cardPanel.add(transactionLogsPanel, "transactionLogs");
        
        // Show the login panel first
        cardLayout.show(cardPanel, "login");
        
        // Add the card panel to the frame
        add(cardPanel);
    }
    
    // Method to switch between panels
    public void showPanel(String panelName) {
        cardLayout.show(cardPanel, panelName);
        
        // Refresh data when switching to specific panels
        if (panelName.equals("dashboard")) {
            dashboardPanel.refreshData();
        } else if (panelName.equals("customerManagement")) {
            customerManagementPanel.refreshData();
        } else if (panelName.equals("transactionLogs")) {
            transactionLogsPanel.refreshData();
        } else if (panelName.equals("transactionManagement")) {
            transactionManagementPanel.refreshCustomerCombos();
        }
        
        // Resize the frame based on which panel is displayed
        if (panelName.equals("login")) {
            setSize(350, 200);  // Smaller size for login
            setLocationRelativeTo(null);  // Re-center the window
        } else if (panelName.equals("dashboard")) {
            setSize(800, 500);  // Larger size for dashboard
            setLocationRelativeTo(null);  // Re-center the window
        } else if (panelName.equals("customerManagement")) {
            setSize(1000, 700);  // Larger size for customer management
            setLocationRelativeTo(null);  // Re-center the window
        } else if (panelName.equals("transactionManagement")) {
            setSize(800, 600);  // Size for transaction management
            setLocationRelativeTo(null);  // Re-center the window
        } else if (panelName.equals("transactionLogs")) {
            setSize(1000, 700);  // Size for transaction logs
            setLocationRelativeTo(null);  // Re-center the window
        }
    }
}

class Auth extends JPanel {

    private JTextField usernameField = new JTextField(15);
    private JPasswordField passwordField = new JPasswordField(15);
    private MainApp mainApp;

    // Constructor with reference to the main container
    public Auth(MainApp mainApp) {    
        this.mainApp = mainApp;
        initializedGUI();
    }
      
    private void initializedGUI() {
        // Set up the panel layout
        setLayout(new BorderLayout());

        // Create a panel with GridLayout for form fields
        JPanel formPanel = new JPanel(new GridLayout(2, 1, 5, 5)); // 2 rows, 2 columns, 10px gaps
        
        JLabel userNameLabel = new JLabel("Username:", JLabel.CENTER);
        JLabel passwordLabel = new JLabel("Password:", JLabel.CENTER);

        // Add components to the form panel
        formPanel.add(userNameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        
        // Create a main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 20)); // 10px horizontal gap, 20px vertical gap
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding
        
        // Create a login button
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(new AuthListener(usernameField, passwordField, mainApp));
        
        // Add components to the main panel
        mainPanel.add(new JLabel("Login to Banking System", JLabel.CENTER), BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(loginButton, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
}

class AuthListener implements ActionListener {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private MainApp mainApp;

    public AuthListener(JTextField usernameField, JPasswordField passwordField, MainApp mainApp) {
        this.usernameField = usernameField;
        this.passwordField = passwordField;
        this.mainApp = mainApp;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
       try {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
               JOptionPane.showMessageDialog(null, 
                         "Username and password cannot be empty", "Invalid Input", JOptionPane.ERROR_MESSAGE);
               return;
            } else if (username.equals("adminx")  && password.equals("12345")) {

                JOptionPane.showMessageDialog(null, 
                         "Login successful", "Login", JOptionPane.INFORMATION_MESSAGE);
                
                // Clear fields for security
                usernameField.setText("");
                passwordField.setText("");
                
                // Switch to dashboard panel using CardLayout
                mainApp.showPanel("dashboard");
            }

       } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, 
                     "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
       }
    }
}

class Dashboard extends JPanel {
    
    private MainApp mainApp;
    private TransactionManager transactionManager;
    private CustomerManager customerManager;
    
    public Dashboard(MainApp mainApp) {
        this.mainApp = mainApp;
        this.transactionManager = TransactionManager.getInstance();
        this.customerManager = CustomerManager.getInstance();
        initializedGUI();
    }    
    
    public void initializedGUI() {
        setLayout(new BorderLayout(10, 10));
        
        // Create header panel with welcome message and user info
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        
        JLabel headerLabel = new JLabel("Banking Dashboard", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setForeground(new Color(0, 102, 204));
        
        JLabel welcomeLabel = new JLabel("Welcome, Admin User", JLabel.LEFT);
        welcomeLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        
        JLabel dateLabel = new JLabel("Last Login: June 4, 2025", JLabel.RIGHT);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        headerPanel.add(headerLabel, BorderLayout.NORTH);
        
        JPanel subHeaderPanel = new JPanel(new BorderLayout());
        subHeaderPanel.add(welcomeLabel, BorderLayout.WEST);
        subHeaderPanel.add(dateLabel, BorderLayout.EAST);
        headerPanel.add(subHeaderPanel, BorderLayout.SOUTH);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Create main split panel
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(200);
        
        // Left navigation panel
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
        
        // Navigation options
        JPanel menuPanel = new JPanel(new GridLayout(6, 1, 0, 5));
        menuPanel.add(createMenuButton("Dashboard", true));
        menuPanel.add(createMenuButton("Customer Management", false));
        menuPanel.add(createMenuButton("Transaction Management", false));
        menuPanel.add(createMenuButton("Transaction Logs", false));
        
        navPanel.add(new JLabel("MENU OPTIONS", JLabel.CENTER), BorderLayout.NORTH);
        navPanel.add(menuPanel, BorderLayout.CENTER);
        
        // Right content panel
        JPanel contentPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 10));
        
        // Add dashboard components with real data
        contentPanel.add(createDashboardPanel("Total Customers", 
                        String.valueOf(customerManager.getCustomerCount()), "Active accounts"));
        contentPanel.add(createDashboardPanel("Total Transactions", 
                        String.valueOf(transactionManager.getTotalTransactionCount()), 
                        "All time"));
        contentPanel.add(createDashboardPanel("Total Deposits", 
                        String.format("$%.2f", transactionManager.getTotalAmountByType("DEPOSIT")), 
                        String.format("%d transactions", transactionManager.getTransactionCountByType("DEPOSIT"))));
        contentPanel.add(createDashboardPanel("Total Withdrawals", 
                        String.format("$%.2f", transactionManager.getTotalAmountByType("WITHDRAW")), 
                        String.format("%d transactions", transactionManager.getTransactionCountByType("WITHDRAW"))));
        
        splitPane.setLeftComponent(navPanel);
        splitPane.setRightComponent(contentPanel);
        
        add(splitPane, BorderLayout.CENTER);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutButton = new JButton("Logout");
        
        // Add logout functionality
        logoutButton.addActionListener((ActionEvent e) -> {
            mainApp.showPanel("login");
        });
        
        buttonPanel.add(logoutButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    public void refreshData() {
        // Re-initialize the GUI to refresh all the statistics
        removeAll();
        initializedGUI();
        revalidate();
        repaint();
    }
    
    private JPanel createDashboardPanel(String title, String value, String details) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder(title));
        
        JLabel valueLabel = new JLabel(value, JLabel.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(valueLabel, BorderLayout.CENTER);
        
        if (details != null && !details.isEmpty()) {
            JLabel detailsLabel = new JLabel(details, JLabel.CENTER);
            detailsLabel.setFont(new Font("Arial", Font.ITALIC, 12));
            panel.add(detailsLabel, BorderLayout.SOUTH);
        }
        
        return panel;
    }
    
    private JButton createMenuButton(String text, boolean isSelected) {
        JButton button = new JButton(text);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        if (isSelected) {
            button.setBackground(new Color(230, 240, 250));
            button.setFont(new Font("Arial", Font.BOLD, 14));
        } else {
            button.setBackground(Color.WHITE);
        }
        
        // Add hover effect
        Color originalColor = button.getBackground();
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!isSelected) {
                    button.setBackground(new Color(245, 245, 245));
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!isSelected) {
                    button.setBackground(originalColor);
                }
            }
        });
        
        button.addActionListener((ActionEvent e) -> {
            // Visual feedback - temporarily change button appearance
            button.setBackground(new Color(200, 230, 255));
            Timer timer = new Timer(150, evt -> {
                if (!isSelected) {
                    button.setBackground(originalColor);
                }
            });
            timer.setRepeats(false);
            timer.start();
            
            if (text.equals("Customer Management")) {
                mainApp.showPanel("customerManagement");
            } else if (text.equals("Transaction Management")) {
                mainApp.showPanel("transactionManagement");
            } else if (text.equals("Transaction Logs")) {
                mainApp.showPanel("transactionLogs");
            } else {
                JOptionPane.showMessageDialog(this, 
                    text + " option selected", "Menu Selection", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        return button;
    }
}

class BankAccount {
    private String accountNumber;
    private String accountHolder;
    private double balance;
    
    public BankAccount(String accountNumber, String accountHolder, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = initialBalance;
    }
    
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }
    
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public String getAccountHolder() {
        return accountHolder;
    }
    
    public String getAccountInfo() {
        return String.format("Account: %s | Holder: %s | Balance: $%.2f", 
                           accountNumber, accountHolder, balance);
    }
}

class Customer {
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

class CustomerManager {
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

class Transaction {
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

class TransactionManager {
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

class CustomerManagement extends JPanel {
    
    private MainApp mainApp;
    private CustomerManager customerManager;
    private DefaultTableModel tableModel;
    private JTable customerTable;
    
    public CustomerManagement(MainApp mainApp) {
        this.mainApp = mainApp;
        this.customerManager = CustomerManager.getInstance();
        initializeGUI();
        refreshTable();
    }
    
    private void initializeGUI() {
        setLayout(new BorderLayout(10, 10));
        
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        
        JLabel headerLabel = new JLabel("Customer Management", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setForeground(new Color(0, 102, 204));
        
        JButton backButton = new JButton("← Back to Dashboard");
        backButton.addActionListener(e -> mainApp.showPanel("dashboard"));
        
        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content panel with tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Customer List Tab
        tabbedPane.addTab("Customer List", createCustomerListPanel());
        
        // Add Customer Tab
        tabbedPane.addTab("Add Customer", createAddCustomerPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel createCustomerListPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Table setup
        String[] columnNames = {"Customer ID", "Name", "Account Number", "Balance", "Email", "Phone"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        customerTable = new JTable(tableModel);
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(customerTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton viewButton = new JButton("View Details");
        JButton editButton = new JButton("Edit Customer");
        JButton deleteButton = new JButton("Delete Customer");
        JButton refreshButton = new JButton("Refresh");
        
        viewButton.addActionListener(e -> viewCustomerDetails());
        editButton.addActionListener(e -> editCustomer());
        deleteButton.addActionListener(e -> deleteCustomer());
        refreshButton.addActionListener(e -> refreshTable());
        
        buttonPanel.add(viewButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createAddCustomerPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Form fields
        JTextField customerIdField = new JTextField(15);
        JTextField nameField = new JTextField(15);
        JTextField emailField = new JTextField(15);
        JTextField phoneField = new JTextField(15);
        JTextArea addressArea = new JTextArea(3, 15);
        JTextField initialBalanceField = new JTextField(15);
        
        // Add components to panel
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Customer ID:"), gbc);
        gbc.gridx = 1;
        panel.add(customerIdField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        panel.add(phoneField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        panel.add(new JScrollPane(addressArea), gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Initial Balance:"), gbc);
        gbc.gridx = 1;
        panel.add(initialBalanceField, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add Customer");
        JButton clearButton = new JButton("Clear Fields");
        
        addButton.addActionListener(e -> {
            if (addCustomer(customerIdField.getText(), nameField.getText(), 
                          emailField.getText(), phoneField.getText(), 
                          addressArea.getText(), initialBalanceField.getText())) {
                // Clear fields after successful addition
                customerIdField.setText("");
                nameField.setText("");
                emailField.setText("");
                phoneField.setText("");
                addressArea.setText("");
                initialBalanceField.setText("");
            }
        });
        
        clearButton.addActionListener(e -> {
            customerIdField.setText("");
            nameField.setText("");
            emailField.setText("");
            phoneField.setText("");
            addressArea.setText("");
            initialBalanceField.setText("");
        });
        
        buttonPanel.add(addButton);
        buttonPanel.add(clearButton);
        
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    private void viewCustomerDetails() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer first.", 
                                        "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Customer customer = customerManager.getAllCustomers().get(selectedRow);
        showCustomerDetailsDialog(customer);
    }
    
    private void showCustomerDetailsDialog(Customer customer) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                                   "Customer Details - " + customer.getName(), true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Customer info panel
        JPanel infoPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Customer Information"));
        
        infoPanel.add(new JLabel("Customer ID:"));
        infoPanel.add(new JLabel(customer.getCustomerId()));
        infoPanel.add(new JLabel("Name:"));
        infoPanel.add(new JLabel(customer.getName()));
        infoPanel.add(new JLabel("Email:"));
        infoPanel.add(new JLabel(customer.getEmail()));
        infoPanel.add(new JLabel("Phone:"));
        infoPanel.add(new JLabel(customer.getPhone()));
        infoPanel.add(new JLabel("Account Number:"));
        infoPanel.add(new JLabel(customer.getAccountNumber()));
        infoPanel.add(new JLabel("Current Balance:"));
        JLabel balanceLabel = new JLabel(String.format("$%.2f", customer.getBalance()));
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        infoPanel.add(balanceLabel);
        
        // Account info panel
        JPanel accountInfoPanel = new JPanel(new FlowLayout());
        accountInfoPanel.setBorder(BorderFactory.createTitledBorder("Account Information"));
        
        JLabel infoLabel = new JLabel("Use Transaction Management for deposits, withdrawals, and transfers");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        accountInfoPanel.add(infoLabel);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(closeButton);
        
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        mainPanel.add(accountInfoPanel, BorderLayout.SOUTH);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
    
    private void editCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer first.", 
                                        "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Customer customer = customerManager.getAllCustomers().get(selectedRow);
        showEditCustomerDialog(customer);
    }
    
    private void showEditCustomerDialog(Customer customer) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                                   "Edit Customer - " + customer.getName(), true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        JTextField nameField = new JTextField(customer.getName(), 15);
        JTextField emailField = new JTextField(customer.getEmail(), 15);
        JTextField phoneField = new JTextField(customer.getPhone(), 15);
        JTextArea addressArea = new JTextArea(customer.getAddress(), 3, 15);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        panel.add(phoneField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        panel.add(new JScrollPane(addressArea), gbc);
        
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.addActionListener(e -> {
            customer.setName(nameField.getText().trim());
            customer.setEmail(emailField.getText().trim());
            customer.setPhone(phoneField.getText().trim());
            customer.setAddress(addressArea.getText().trim());
            refreshTable();
            dialog.dispose();
            JOptionPane.showMessageDialog(this, "Customer updated successfully!", 
                                        "Update Successful", JOptionPane.INFORMATION_MESSAGE);
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void deleteCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer first.", 
                                        "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Customer customer = customerManager.getAllCustomers().get(selectedRow);
        int result = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete customer: " + customer.getName() + "?", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            customerManager.removeCustomer(customer);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Customer deleted successfully!", 
                                        "Delete Successful", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private boolean addCustomer(String customerId, String name, String email, 
                              String phone, String address, String initialBalanceStr) {
        // Validation
        if (customerId.trim().isEmpty() || name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Customer ID and Name are required.", 
                                        "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Check for duplicate customer ID
        if (customerManager.customerExists(customerId.trim())) {
            JOptionPane.showMessageDialog(this, "Customer ID already exists.", 
                                        "Duplicate ID", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            double initialBalance = initialBalanceStr.trim().isEmpty() ? 0.0 : 
                                  Double.parseDouble(initialBalanceStr.trim());
            
            Customer newCustomer = new Customer(customerId.trim(), name.trim(), 
                                              email.trim(), phone.trim(), address.trim(), initialBalance);
            customerManager.addCustomer(newCustomer);
            refreshTable();
            
            JOptionPane.showMessageDialog(this, "Customer added successfully!", 
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid initial balance.", 
                                        "Invalid Balance", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public void refreshData() {
        refreshTable();
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Customer customer : customerManager.getAllCustomers()) {
            Object[] row = {
                customer.getCustomerId(),
                customer.getName(),
                customer.getAccountNumber(),
                String.format("$%.2f", customer.getBalance()),
                customer.getEmail(),
                customer.getPhone()
            };
            tableModel.addRow(row);
        }
    }
}

class TransactionManagement extends JPanel {
    
    private MainApp mainApp;
    private TransactionManager transactionManager;
    private CustomerManager customerManager;
    
    public TransactionManagement(MainApp mainApp) {
        this.mainApp = mainApp;
        this.transactionManager = TransactionManager.getInstance();
        this.customerManager = CustomerManager.getInstance();
        initializeGUI();
        refreshCustomerCombos();
    }
    
    private void initializeGUI() {
        setLayout(new BorderLayout(10, 10));
        
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        
        JLabel headerLabel = new JLabel("Transaction Management", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setForeground(new Color(0, 102, 204));
        
        JButton backButton = new JButton("← Back to Dashboard");
        backButton.addActionListener(e -> mainApp.showPanel("dashboard"));
        
        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content panel with tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Deposit Tab
        tabbedPane.addTab("Deposit", createDepositPanel());
        
        // Withdraw Tab
        tabbedPane.addTab("Withdraw", createWithdrawPanel());
        
        // Transfer Tab
        tabbedPane.addTab("Transfer", createTransferPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel createDepositPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        JComboBox<Customer> customerCombo = new JComboBox<>();
        JTextField amountField = new JTextField(15);
        JTextArea descriptionArea = new JTextArea(3, 15);
        descriptionArea.setText("Deposit transaction");
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Select Customer:"), gbc);
        gbc.gridx = 1;
        panel.add(customerCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Amount:"), gbc);
        gbc.gridx = 1;
        panel.add(amountField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        panel.add(new JScrollPane(descriptionArea), gbc);
        
        JButton depositButton = new JButton("Process Deposit");
        depositButton.setBackground(new Color(76, 175, 80));
        depositButton.setForeground(Color.WHITE);
        depositButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        depositButton.addActionListener(e -> {
            processDeposit(customerCombo, amountField, descriptionArea);
        });
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(depositButton, gbc);
        
        return panel;
    }
    
    private JPanel createWithdrawPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        JComboBox<Customer> customerCombo = new JComboBox<>();
        JTextField amountField = new JTextField(15);
        JTextArea descriptionArea = new JTextArea(3, 15);
        descriptionArea.setText("Withdrawal transaction");
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Select Customer:"), gbc);
        gbc.gridx = 1;
        panel.add(customerCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Amount:"), gbc);
        gbc.gridx = 1;
        panel.add(amountField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        panel.add(new JScrollPane(descriptionArea), gbc);
        
        JButton withdrawButton = new JButton("Process Withdrawal");
        withdrawButton.setBackground(new Color(244, 67, 54));
        withdrawButton.setForeground(Color.WHITE);
        withdrawButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        withdrawButton.addActionListener(e -> {
            processWithdraw(customerCombo, amountField, descriptionArea);
        });
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(withdrawButton, gbc);
        
        return panel;
    }
    
    private JPanel createTransferPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        JComboBox<Customer> fromCustomerCombo = new JComboBox<>();
        JComboBox<Customer> toCustomerCombo = new JComboBox<>();
        JTextField amountField = new JTextField(15);
        JTextArea descriptionArea = new JTextArea(3, 15);
        descriptionArea.setText("Transfer transaction");
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("From Customer:"), gbc);
        gbc.gridx = 1;
        panel.add(fromCustomerCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("To Customer:"), gbc);
        gbc.gridx = 1;
        panel.add(toCustomerCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Amount:"), gbc);
        gbc.gridx = 1;
        panel.add(amountField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        panel.add(new JScrollPane(descriptionArea), gbc);
        
        JButton transferButton = new JButton("Process Transfer");
        transferButton.setBackground(new Color(33, 150, 243));
        transferButton.setForeground(Color.WHITE);
        transferButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        transferButton.addActionListener(e -> {
            processTransfer(fromCustomerCombo, toCustomerCombo, amountField, descriptionArea);
        });
        
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(transferButton, gbc);
        
        return panel;
    }
    
    private void processDeposit(JComboBox<Customer> customerCombo, JTextField amountField, JTextArea descriptionArea) {
        try {
            Customer customer = (Customer) customerCombo.getSelectedItem();
            if (customer == null) {
                JOptionPane.showMessageDialog(this, "Please select a customer.", "No Customer Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            double amount = Double.parseDouble(amountField.getText().trim());
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be positive.", "Invalid Amount", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            customer.getAccount().deposit(amount);
            
            // Create transaction record
            String transactionId = transactionManager.generateTransactionId();
            Transaction transaction = new Transaction(transactionId, customer.getAccountNumber(), 
                                                    customer.getName(), "DEPOSIT", amount, descriptionArea.getText().trim());
            transactionManager.addTransaction(transaction);
            
            JOptionPane.showMessageDialog(this, 
                String.format("Deposit of $%.2f completed successfully!\nNew Balance: $%.2f", 
                            amount, customer.getBalance()),
                "Deposit Successful", JOptionPane.INFORMATION_MESSAGE);
            
            // Clear fields
            amountField.setText("");
            descriptionArea.setText("Deposit transaction");
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void processWithdraw(JComboBox<Customer> customerCombo, JTextField amountField, JTextArea descriptionArea) {
        try {
            Customer customer = (Customer) customerCombo.getSelectedItem();
            if (customer == null) {
                JOptionPane.showMessageDialog(this, "Please select a customer.", "No Customer Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            double amount = Double.parseDouble(amountField.getText().trim());
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be positive.", "Invalid Amount", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (customer.getAccount().withdraw(amount)) {
                // Create transaction record
                String transactionId = transactionManager.generateTransactionId();
                Transaction transaction = new Transaction(transactionId, customer.getAccountNumber(), 
                                                        customer.getName(), "WITHDRAW", amount, descriptionArea.getText().trim());
                transactionManager.addTransaction(transaction);
                
                JOptionPane.showMessageDialog(this, 
                    String.format("Withdrawal of $%.2f completed successfully!\nNew Balance: $%.2f", 
                                amount, customer.getBalance()),
                    "Withdrawal Successful", JOptionPane.INFORMATION_MESSAGE);
                
                // Clear fields
                amountField.setText("");
                descriptionArea.setText("Withdrawal transaction");
            } else {
                JOptionPane.showMessageDialog(this, "Insufficient funds for this withdrawal.", 
                                            "Withdrawal Failed", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void processTransfer(JComboBox<Customer> fromCustomerCombo, JComboBox<Customer> toCustomerCombo, 
                               JTextField amountField, JTextArea descriptionArea) {
        try {
            Customer fromCustomer = (Customer) fromCustomerCombo.getSelectedItem();
            Customer toCustomer = (Customer) toCustomerCombo.getSelectedItem();
            
            if (fromCustomer == null || toCustomer == null) {
                JOptionPane.showMessageDialog(this, "Please select both customers.", "Missing Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (fromCustomer.getCustomerId().equals(toCustomer.getCustomerId())) {
                JOptionPane.showMessageDialog(this, "Cannot transfer to the same account.", "Invalid Transfer", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double amount = Double.parseDouble(amountField.getText().trim());
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be positive.", "Invalid Amount", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (fromCustomer.getAccount().withdraw(amount)) {
                toCustomer.getAccount().deposit(amount);
                
                // Create transaction record
                String transactionId = transactionManager.generateTransactionId();
                Transaction transaction = new Transaction(transactionId, fromCustomer.getAccountNumber(), 
                                                        fromCustomer.getName(), toCustomer.getAccountNumber(), 
                                                        toCustomer.getName(), amount, descriptionArea.getText().trim());
                transactionManager.addTransaction(transaction);
                
                JOptionPane.showMessageDialog(this, 
                    String.format("Transfer of $%.2f from %s to %s completed successfully!\nFrom Account Balance: $%.2f\nTo Account Balance: $%.2f", 
                                amount, fromCustomer.getName(), toCustomer.getName(), 
                                fromCustomer.getBalance(), toCustomer.getBalance()),
                    "Transfer Successful", JOptionPane.INFORMATION_MESSAGE);
                
                // Clear fields
                amountField.setText("");
                descriptionArea.setText("Transfer transaction");
            } else {
                JOptionPane.showMessageDialog(this, "Insufficient funds for this transfer.", 
                                            "Transfer Failed", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void refreshCustomerCombos() {
        refreshCustomerComboInPanel(this);
    }
    
    @SuppressWarnings("unchecked")
    private void refreshCustomerComboInPanel(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JComboBox) {
                JComboBox<Customer> combo = (JComboBox<Customer>) component;
                combo.removeAllItems();
                for (Customer customer : customerManager.getAllCustomers()) {
                    combo.addItem(customer);
                }
            } else if (component instanceof Container) {
                refreshCustomerComboInPanel((Container) component);
            }
        }
    }
}

class TransactionLogs extends JPanel {
    
    private MainApp mainApp;
    private TransactionManager transactionManager;
    private DefaultTableModel tableModel;
    private JTable transactionTable;
    
    public TransactionLogs(MainApp mainApp) {
        this.mainApp = mainApp;
        this.transactionManager = TransactionManager.getInstance();
        initializeGUI();
        loadTransactions();
    }
    
    private void initializeGUI() {
        setLayout(new BorderLayout(10, 10));
        
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        
        JLabel headerLabel = new JLabel("Transaction Logs", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setForeground(new Color(0, 102, 204));
        
        JButton backButton = new JButton("← Back to Dashboard");
        backButton.addActionListener(e -> mainApp.showPanel("dashboard"));
        
        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Table setup
        String[] columnNames = {"Transaction ID", "Date/Time", "Type", "Customer", "Amount", "Description"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        transactionTable = new JTable(tableModel);
        transactionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        transactionTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadTransactions());
        buttonPanel.add(refreshButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadTransactions() {
        tableModel.setRowCount(0);
        List<Transaction> transactions = transactionManager.getAllTransactions();
        
        for (Transaction transaction : transactions) {
            Object[] row = new Object[6];
            row[0] = transaction.getTransactionId();
            row[1] = transaction.getFormattedTimestamp();
            row[2] = transaction.getTransactionType();
            row[3] = transaction.getFromCustomerName();
            row[4] = transaction.getFormattedAmount();
            row[5] = transaction.getDescription();
            
            tableModel.addRow(row);
        }
    }
    
    public void refreshData() {
        loadTransactions();
    }
}