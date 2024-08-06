import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String name;
    private String email;
    private String password;
    private double balance;
    private List<String> transactionHistory;

    public Customer(String name, String email, String password) { 
        this.name = name;
        this.email = email;
        this.password = password;
        this.balance = 0.0;
        this.transactionHistory = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
    

    public String getPassword() {
        return password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<String> getTransactionHistory() {
        return transactionHistory;
    }

    public void deposit(double amount) {
        balance += amount;
        transactionHistory.add("Deposited: $" + amount);
    }

    public boolean withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            transactionHistory.add("Withdrew: $" + amount);
            return true;
        } else {
            return false;
        }
    }

    public void addTransaction(String transaction) {
        transactionHistory.add(transaction);
    }
}
