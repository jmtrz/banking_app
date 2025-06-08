package ui;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TransactionLogs extends JPanel {
    
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
