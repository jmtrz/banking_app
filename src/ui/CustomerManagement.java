package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CustomerManagement extends JPanel {
    
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
