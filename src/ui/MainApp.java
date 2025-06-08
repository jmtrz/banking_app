package ui;

import java.awt.*;
import javax.swing.*;

public class MainApp extends JFrame {
    
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
    }    // Method to switch between panels
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
