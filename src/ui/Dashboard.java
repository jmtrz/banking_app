package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Dashboard extends JPanel {
    
    private MainApp mainApp;
    
    public Dashboard(MainApp mainApp) {
        this.mainApp = mainApp;
        initializedGUI();
    }    public void initializedGUI() {
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
        menuPanel.add(createMenuButton("Account Summary", true));
        menuPanel.add(createMenuButton("Transactions", false));
        menuPanel.add(createMenuButton("Transfer Funds", false));
        menuPanel.add(createMenuButton("Bill Payment", false));
        menuPanel.add(createMenuButton("Account Settings", false));
        menuPanel.add(createMenuButton("Customer Support", false));
        
        navPanel.add(new JLabel("MENU OPTIONS", JLabel.CENTER), BorderLayout.NORTH);
        navPanel.add(menuPanel, BorderLayout.CENTER);
        
        // Right content panel
        JPanel contentPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 10));
        
        // Add some dashboard components with more details
        contentPanel.add(createDashboardPanel("Account Balance", "$10,000.75", "Available: $9,850.25"));
        contentPanel.add(createDashboardPanel("Recent Transactions", "5", "Last: Coffee Shop - $4.50"));
        contentPanel.add(createDashboardPanel("Pending Payments", "2", "Next: Electricity - $125.00"));
        contentPanel.add(createDashboardPanel("Savings Goal", "75%", "Target: $15,000 by Dec 2025"));
        
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
      private JPanel createDashboardPanel(String title, String value) {
        return createDashboardPanel(title, value, null);
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
        
        if (isSelected) {
            button.setBackground(new Color(230, 240, 250));
            button.setFont(new Font("Arial", Font.BOLD, 14));
        }
          button.addActionListener((ActionEvent e) -> {
            JOptionPane.showMessageDialog(this, 
                text + " option selected", "Menu Selection", JOptionPane.INFORMATION_MESSAGE);
        });
        
        return button;
    }
}
