import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeDashboard extends JFrame {
    @SuppressWarnings("unused")
    private Employee employee;
    private JTextArea customerProfilesArea;

    public EmployeeDashboard(Employee employee) {
        this.employee = employee;

        setTitle("Employee Dashboard");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel welcomeLabel = new JLabel("Welcome, " + employee.getName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(welcomeLabel, gbc);

        JButton viewProfilesButton = new JButton("View Customer Profiles");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        viewProfilesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewCustomerProfiles();
            }
        });
        add(viewProfilesButton, gbc);

        customerProfilesArea = new JTextArea(15, 40);
        customerProfilesArea.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(new JScrollPane(customerProfilesArea), gbc);
    }

    private void viewCustomerProfiles() {
        customerProfilesArea.setText("");
        try {
            ResultSet resultSet = DatabaseConnection.getCustomerProfiles();
            while (resultSet.next()) {
                customerProfilesArea.append(
                        "Name: " + resultSet.getString("name") +
                        "\nEmail: " + resultSet.getString("email") +
                        "\nBalance: " + resultSet.getDouble("balance") +
                        "\n\n"
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
