package events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import ui.MainApp;

public class AuthListener implements ActionListener {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private MainApp mainApp;

    public AuthListener(JTextField usernameField, JPasswordField passwordField, MainApp mainApp) {
        this.usernameField = usernameField;
        this.passwordField = passwordField;
        this.mainApp = mainApp;
    }    @Override
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