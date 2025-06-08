package ui;



import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class Dashboard extends JPanel {
    
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
    
    // private JPanel createDashboardPanel(String title, String value) {
    //     return createDashboardPanel(title, value, null);
    // }
    
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
