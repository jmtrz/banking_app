package ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class TransactionManagement extends JPanel {
    
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
