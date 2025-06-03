import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BankingGUI extends JFrame {
    private BankAccount account;
    private JLabel balanceLabel;
    private JTextField amountField;
    private JTextArea transactionHistory;
    
    public BankingGUI() {
        account = new BankAccount("12345", "John Doe", 1000.00);
        initializeGUI();
    }
    
    private void initializeGUI() {
        setTitle("Simple Banking Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        JPanel topPanel = createTopPanel();
        JPanel centerPanel = createCenterPanel();
        JPanel bottomPanel = createBottomPanel();
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Account Information"));
        
        JLabel accountInfo = new JLabel(account.getAccountInfo());
        balanceLabel = new JLabel(String.format("Current Balance: $%.2f", account.getBalance()));
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        panel.add(accountInfo);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(balanceLabel);
        
        return panel;
    }
    
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Transactions"));
        
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Amount: $"));
        amountField = new JTextField(10);
        inputPanel.add(amountField);
        
        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");
        
        depositButton.addActionListener(new DepositListener());
        withdrawButton.addActionListener(new WithdrawListener());
        
        inputPanel.add(depositButton);
        inputPanel.add(withdrawButton);
        
        transactionHistory = new JTextArea(10, 30);
        transactionHistory.setEditable(false);
        transactionHistory.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(transactionHistory);
        
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        addTransaction("Account opened with initial balance: $" + account.getBalance());
        
        return panel;
    }
    
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));
        panel.add(exitButton);
        return panel;
    }
    
    private void updateBalance() {
        balanceLabel.setText(String.format("Current Balance: $%.2f", account.getBalance()));
    }
    
    private void addTransaction(String transaction) {
        transactionHistory.append(transaction + "\n");
        transactionHistory.setCaretPosition(transactionHistory.getDocument().getLength());
    }
    
    private class DepositListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                double amount = Double.parseDouble(amountField.getText());
                account.deposit(amount);
                updateBalance();
                addTransaction(String.format("Deposited: $%.2f", amount));
                amountField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(BankingGUI.this, 
                    "Please enter a valid amount", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private class WithdrawListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                double amount = Double.parseDouble(amountField.getText());
                if (account.withdraw(amount)) {
                    updateBalance();
                    addTransaction(String.format("Withdrew: $%.2f", amount));
                    amountField.setText("");
                } else {
                    JOptionPane.showMessageDialog(BankingGUI.this, 
                        "Insufficient funds or invalid amount", "Transaction Failed", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(BankingGUI.this, 
                    "Please enter a valid amount", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}