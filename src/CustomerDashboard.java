import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDashboard extends JFrame {
    private Customer customer;
    private JLabel balanceLabel;
    private JTextArea transactionHistoryArea;

    public CustomerDashboard(Customer customer) {
        this.customer = customer;

        setTitle("Customer Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel welcomeLabel = new JLabel("Welcome, " + customer.getName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(welcomeLabel, gbc);

        JLabel balanceTextLabel = new JLabel("Current Balance:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(balanceTextLabel, gbc);

        balanceLabel = new JLabel(String.valueOf(customer.getBalance()));
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(balanceLabel, gbc);

        JButton depositButton = new JButton("Deposit");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDeposit();
            }
        });
        add(depositButton, gbc);

        JButton withdrawButton = new JButton("Withdraw");
        gbc.gridx = 1;
        gbc.gridy = 2;
        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleWithdraw();
            }
        });
        add(withdrawButton, gbc);

        JButton viewDetailsButton = new JButton("View Account Details");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        viewDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAccountDetails();
            }
        });
        add(viewDetailsButton, gbc);

        JButton viewHistoryButton = new JButton("View Transaction History");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        viewHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewTransactionHistory();
            }
        });
        add(viewHistoryButton, gbc);

        transactionHistoryArea = new JTextArea(10, 30);
        transactionHistoryArea.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        add(new JScrollPane(transactionHistoryArea), gbc);
    }

    private void handleDeposit() {
        String amountStr = JOptionPane.showInputDialog(this, "Enter amount to deposit:");
        if (amountStr != null) {
            double amount = Double.parseDouble(amountStr);
            customer.deposit(amount);
            balanceLabel.setText(String.valueOf(customer.getBalance()));
            try {
                DatabaseConnection.updateBalance(customer.getEmail(), customer.getBalance());
                DatabaseConnection.addTransaction(customer.getEmail(), "Deposited: $" + amount);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleWithdraw() {
        String amountStr = JOptionPane.showInputDialog(this, "Enter amount to withdraw:");
        if (amountStr != null) {
            double amount = Double.parseDouble(amountStr);
            if (customer.withdraw(amount)) {
                balanceLabel.setText(String.valueOf(customer.getBalance()));
                try {
                    DatabaseConnection.updateBalance(customer.getEmail(), customer.getBalance());
                    DatabaseConnection.addTransaction(customer.getEmail(), "Withdrew: $" + amount);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Insufficient balance!");
            }
        }
    }

    private void viewAccountDetails() {
        String message = "Name: " + customer.getName() +
                "\nEmail: " + customer.getEmail() +
                "\nBalance: $" + customer.getBalance();
        JOptionPane.showMessageDialog(this, message);
    }

    private void viewTransactionHistory() {
        transactionHistoryArea.setText("");
        try {
            ResultSet resultSet = DatabaseConnection.getTransactionHistory(customer.getEmail());
            while (resultSet.next()) {
                transactionHistoryArea.append(resultSet.getString("transaction") + "\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    private void depositMoney() {
        String amountStr = JOptionPane.showInputDialog(this, "Enter amount to deposit:");
        if (amountStr != null) {
            try {
                double amount = Double.parseDouble(amountStr);
                if (amount > 0) {
                    double newBalance = customer.getBalance() + amount;
                    DatabaseConnection.updateBalance(customer.getEmail(), newBalance);
                    customer.setBalance(newBalance);
                    JOptionPane.showMessageDialog(this, "Deposit successful!");
                } else {
                    JOptionPane.showMessageDialog(this, "Amount must be positive!");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid amount format!");
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to deposit money!");
            }
        }
    }
    
    @SuppressWarnings("unused")
    private void withdrawMoney() {
        String amountStr = JOptionPane.showInputDialog(this, "Enter amount to withdraw:");
        if (amountStr != null) {
            try {
                double amount = Double.parseDouble(amountStr);
                if (amount > 0) {
                    if (customer.getBalance() >= amount) {
                        double newBalance = customer.getBalance() - amount;
                        DatabaseConnection.updateBalance(customer.getEmail(), newBalance);
                        customer.setBalance(newBalance);
                        JOptionPane.showMessageDialog(this, "Withdrawal successful!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Insufficient funds!");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Amount must be positive!");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid amount format!");
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to withdraw money!");
            }
        }
    }
    
    
}
