package ui;

import javax.swing.*;
import java.awt.*;

import events.AuthListener;

public class Auth extends JPanel {

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

