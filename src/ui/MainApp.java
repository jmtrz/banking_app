package ui;

import javax.swing.*;
import java.awt.*;

public class MainApp extends JFrame {
    
    // CardLayout and container panel
    private CardLayout cardLayout;
    private JPanel cardPanel;
    
    // The different screens/panels
    private Auth loginPanel;
    private Dashboard dashboardPanel;
    
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
        
        // Add panels to the card layout
        cardPanel.add(loginPanel, "login");
        cardPanel.add(dashboardPanel, "dashboard");
        
        // Show the login panel first
        cardLayout.show(cardPanel, "login");
        
        // Add the card panel to the frame
        add(cardPanel);
    }    // Method to switch between panels
    public void showPanel(String panelName) {
        cardLayout.show(cardPanel, panelName);
        
        // Resize the frame based on which panel is displayed
        if (panelName.equals("login")) {
            setSize(350, 200);  // Smaller size for login
            setLocationRelativeTo(null);  // Re-center the window
        } else if (panelName.equals("dashboard")) {
            setSize(800, 500);  // Larger size for dashboard
            setLocationRelativeTo(null);  // Re-center the window
        }
    }
}
