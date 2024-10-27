import java.sql.*;
import java.util.Scanner;

public class InventoryManagementSystem {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/inventory";
    private static final String DB_USERNAME = "your_username";
    private static final String DB_PASSWORD = "your_password";

    private static final String ADD_PRODUCT_QUERY = "INSERT INTO Products (Name, Description, Quantity, Price) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_PRODUCT_QUERY = "UPDATE Products SET Quantity = ? WHERE ProductID = ?";
    private static final String DELETE_PRODUCT_QUERY = "DELETE FROM Products WHERE ProductID = ?";
    private static final String GET_ALL_PRODUCTS_QUERY = "SELECT * FROM Products";
    private static final String PLACE_ORDER_QUERY = "INSERT INTO Orders (ProductID, Quantity, OrderDate) VALUES (?, ?, ?)";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            System.out.println("Inventory Management System");
            System.out.println("1. Add Product");
            System.out.println("2. Update Product");
            System.out.println("3. Delete Product");
            System.out.println("4. Get All Products");
            System.out.println("5. Place Order");
            System.out.println("6. Exit");

            int choice;
            do {
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        addProduct(connection, scanner);
                        break;
                    case 2:
                        updateProduct(connection, scanner);
                        break;
                    case 3:
                        deleteProduct(connection, scanner);
                        break;
                    case 4:
                        getAllProducts(connection);
                        break;
                    case 5:
                        placeOrder(connection, scanner);
                        break;
                    case 6:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } while (choice != 6);

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }

    private static void addProduct(Connection connection, Scanner scanner) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(ADD_PRODUCT_QUERY);
        System.out.print("Enter product name: ");
        statement.setString(1, scanner.nextLine());
        System.out.print("Enter product description: ");
        statement.setString(2, scanner.nextLine());
        System.out.print("Enter product quantity: ");
        statement.setInt(3, scanner.nextInt());
        System.out.print("Enter product price: ");
        statement.setDouble(4, scanner.nextDouble());
        scanner.nextLine(); // Consume newline
        statement.executeUpdate();
        System.out.println("Product added successfully!");
    }

    private static void updateProduct(Connection connection, Scanner scanner) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(UPDATE_PRODUCT_QUERY);
        System.out.print("Enter product ID: ");
        int productId = scanner.nextInt();
        System.out.print("Enter new quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        statement.setInt(1, quantity);
        statement.setInt(2, productId);
        statement.executeUpdate();
        System.out.println("Product updated successfully!");
    }

    private static void deleteProduct(Connection connection, Scanner scanner) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(DELETE_PRODUCT_QUERY);
        System.out.print("Enter product ID: ");
        statement.setInt(1, scanner.nextInt());
        scanner.nextLine(); // Consume newline
        statement.executeUpdate();
        System.out.println("Product deleted successfully!");
    }

    private static void getAllProducts(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(GET_ALL_PRODUCTS_QUERY);
        while (resultSet.next()) {
            System.out.println("Product ID: " + resultSet.getInt("ProductID"));
            System.out.println("Name: " + resultSet.getString("Name"));
            System.out.println("Description: " + resultSet.getString("Description"));
            System.out.println("Quantity: " + resultSet.getInt("Quantity"));
            System.out.println("Price: " + resultSet.getDouble("Price"));
            System.out.println("------------------------------");
        }
    }

    private static void placeOrder(Connection connection, Scanner scanner) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(PLACE_ORDER_QUERY);
        System.out.print("Enter product ID: ");
        int productId = scanner.nextInt();
        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        java.sql.Date orderDate = new java.sql.Date(System.currentTimeMillis());
        statement.setInt(1, productId);
        statement.setInt(2, quantity);
        statement.setDate(3, orderDate);
        statement.executeUpdate();

        System.out.println("Order placed successfully!");
    }
}


