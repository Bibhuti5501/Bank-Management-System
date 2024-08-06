import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/bank_management";
    private static final String USER = "root";
    private static final String PASSWORD = "bibhuti3740"; // Update with your MySQL password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static ResultSet getCustomerProfiles() throws SQLException {
        Connection connection = getConnection();
        String sql = "SELECT name, email, balance FROM customers";
        PreparedStatement statement = connection.prepareStatement(sql);
        return statement.executeQuery();
    }

    public static ResultSet getEmployeeProfiles() throws SQLException {
        Connection connection = getConnection();
        String sql = "SELECT employee_id, name, position FROM employees";
        PreparedStatement statement = connection.prepareStatement(sql);
        return statement.executeQuery();
    }

    public static void addEmployee(String employeeId, String name, String position, String password) throws SQLException {
        Connection connection = getConnection();
        String sql = "INSERT INTO employees (employee_id, name, position, password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, employeeId);
            statement.setString(2, name);
            statement.setString(3, position);
            statement.setString(4, password);
            statement.executeUpdate();
        }
    }

    public static void removeEmployee(String employeeId) throws SQLException {
        Connection connection = getConnection();
        String sql = "DELETE FROM employees WHERE employee_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, employeeId);
            statement.executeUpdate();
        }
    }
    public static void updateBalance(String email, double balance) throws SQLException {
        try (Connection connection = getConnection()) {
            String sql = "UPDATE customers SET balance = ? WHERE email = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setDouble(1, balance);
                statement.setString(2, email);
                statement.executeUpdate();
            }
        }
    }

    public static void addTransaction(String email, String transaction) throws SQLException {
        try (Connection connection = getConnection()) {
            String sql = "INSERT INTO transactions (email, transaction) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, email);
                statement.setString(2, transaction);
                statement.executeUpdate();
            }
        }
    }

    public static ResultSet getTransactionHistory(String email) throws SQLException {
        Connection connection = getConnection();
        String sql = "SELECT transaction FROM transactions WHERE email = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, email);
        return statement.executeQuery();
    }

    public static void applyMonthlyInterest() throws SQLException {
        Connection connection = getConnection();
        String sql = "UPDATE customers SET balance = balance + (balance * 0.02)"; // Assuming 2% interest rate
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        }
    }
    
    
}
