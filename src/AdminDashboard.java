import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDashboard extends JFrame {
    @SuppressWarnings("unused")
    private Admin admin;
    private JTextArea profilesArea;

    public AdminDashboard(Admin admin) {
        this.admin = admin;

        setTitle("Admin Dashboard");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel welcomeLabel = new JLabel("Welcome, " + admin.getName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(welcomeLabel, gbc);

        JButton viewCustomerProfilesButton = new JButton("View Customer Profiles");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        viewCustomerProfilesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewCustomerProfiles();
            }
        });
        add(viewCustomerProfilesButton, gbc);

        JButton viewEmployeeProfilesButton = new JButton("View Employee Profiles");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        viewEmployeeProfilesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewEmployeeProfiles();
            }
        });
        add(viewEmployeeProfilesButton, gbc);

        JButton manageEmployeesButton = new JButton("Manage Employees");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        manageEmployeesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manageEmployees();
            }
        });
        add(manageEmployeesButton, gbc);

        profilesArea = new JTextArea(20, 50);
        profilesArea.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(new JScrollPane(profilesArea), gbc);
    }

    private void viewCustomerProfiles() {
        profilesArea.setText("");
        try {
            ResultSet resultSet = DatabaseConnection.getCustomerProfiles();
            while (resultSet.next()) {
                profilesArea.append(
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

    private void viewEmployeeProfiles() {
        profilesArea.setText("");
        try {
            ResultSet resultSet = DatabaseConnection.getEmployeeProfiles();
            while (resultSet.next()) {
                profilesArea.append(
                        "Employee ID: " + resultSet.getString("employee_id") +
                        "\nName: " + resultSet.getString("name") +
                        "\nPosition: " + resultSet.getString("position") +
                        "\n\n"
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void manageEmployees() {
        String[] options = {"Add Employee", "Remove Employee"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Choose an action:",
                "Manage Employees",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            addEmployee();
        } else if (choice == 1) {
            removeEmployee();
        }
    }

    private void addEmployee() {
        String employeeId = JOptionPane.showInputDialog(this, "Enter Employee ID:");
        String name = JOptionPane.showInputDialog(this, "Enter Employee Name:");
        String position = JOptionPane.showInputDialog(this, "Enter Employee Position:");
        String password = JOptionPane.showInputDialog(this, "Enter Employee Password:");

        if (employeeId != null && name != null && position != null && password != null) {
            try {
                DatabaseConnection.addEmployee(employeeId, name, position, password);
                JOptionPane.showMessageDialog(this, "Employee added successfully!");
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to add employee!");
            }
        }
    }

    private void removeEmployee() {
        String employeeId = JOptionPane.showInputDialog(this, "Enter Employee ID to remove:");

        if (employeeId != null) {
            try {
                DatabaseConnection.removeEmployee(employeeId);
                JOptionPane.showMessageDialog(this, "Employee removed successfully!");
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to remove employee!");
            }
        }
    }
}
