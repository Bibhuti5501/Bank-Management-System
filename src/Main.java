import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        // Create and show the GUI for login options
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Bank Management System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setLocationRelativeTo(null);

            JButton customerLoginButton = new JButton("Customer Login");
            JButton employeeLoginButton = new JButton("Employee Login");
            JButton adminLoginButton = new JButton("Admin Login");

            customerLoginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new LoginRegisterGUI().setVisible(true);
                }
            });

            employeeLoginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new EmployeeLoginGUI().setVisible(true);
                }
            });

            adminLoginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new AdminLoginGUI().setVisible(true);
                }
            });

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(3, 1));
            panel.add(customerLoginButton);
            panel.add(employeeLoginButton);
            panel.add(adminLoginButton);

            frame.getContentPane().add(panel);
            frame.setVisible(true);
        });

        // Start scheduler for monthly interest calculation
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            try {
                DatabaseConnection.applyMonthlyInterest();
                System.out.println("Monthly interest applied.");
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Failed to apply monthly interest.");
            }
        }, 0, 1, TimeUnit.DAYS);
    }
}
