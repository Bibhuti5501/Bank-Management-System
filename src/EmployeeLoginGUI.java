import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeLoginGUI extends JFrame {
    private JTextField employeeIdField;
    private JPasswordField passwordField;

    public EmployeeLoginGUI() {
        setTitle("Employee Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel employeeIdLabel = new JLabel("Employee ID:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(employeeIdLabel, gbc);

        employeeIdField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(employeeIdField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        add(loginButton, gbc);
    }

    private void handleLogin() {
        String employeeId = employeeIdField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM employees WHERE employee_id = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, employeeId);
                statement.setString(2, password);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    Employee employee = new Employee(
                            resultSet.getString("employee_id"),
                            resultSet.getString("name"),
                            resultSet.getString("position"));
                    EmployeeDashboard employeeDashboard = new EmployeeDashboard(employee);
                    employeeDashboard.setVisible(true);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Employee ID or Password!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Login failed! Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EmployeeLoginGUI employeeLoginGUI = new EmployeeLoginGUI();
            employeeLoginGUI.setVisible(true);
        });
    }
}
