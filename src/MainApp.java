import java.util.List;
import java.util.Scanner;
import model.User;
import model.Product;
import model.CartItem;
import model.Order;
import model.OrderItem;
import dao.*;

public class MainApp {
    private static Scanner scanner = new Scanner(System.in);
    private static UserDAO userDAO = new UserDAO();
    private static ProductDAO productDAO = new ProductDAO();
    private static CartDAO cartDAO = new CartDAO();
    private static OrderDAO orderDAO = new OrderDAO();
    private static User currentUser = null;

    public static void main(String[] args) {
        System.out.println("Welcome to Online Shopping System");

        while (true) {
            if (currentUser == null) {
                showMainMenu();
            } else {
                showUserMenu();
            }
        }
    }

    private static void showMainMenu() {
        System.out.println("\n1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (choice) {
            case 1:
                registerUser();
                break;
            case 2:
                loginUser();
                break;
            case 3:
                System.out.println("Thank you for using our system. Goodbye!");
                System.exit(0);
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void showUserMenu() {
        System.out.println("\nWelcome, " + currentUser.getUsername() + "!");
        System.out.println("1. View Products");
        System.out.println("2. View Cart");
        System.out.println("3. View Orders");
        System.out.println("4. Logout");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (choice) {
            case 1:
                viewProducts();
                break;
            case 2:
                viewCart();
                break;
            case 3:
                viewOrders();
                break;
            case 4:
                currentUser = null;
                System.out.println("Logged out successfully.");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void registerUser() {
        System.out.println("\n--- User Registration ---");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter address: ");
        String address = scanner.nextLine();

        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();

        User newUser = new User(username, password, email, address, phone);
        boolean success = userDAO.registerUser(newUser);

        if (success) {
            System.out.println("Registration successful! You can now login.");
        } else {
            System.out.println("Registration failed. Username or email might already exist.");
        }
    }

    private static void loginUser() {
        System.out.println("\n--- User Login ---");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = userDAO.loginUser(username, password);

        if (user != null) {
            currentUser = user;
            System.out.println("Login successful! Welcome, " + user.getUsername() + ".");
        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }

    private static void viewProducts() {
        List<Product> products = productDAO.getAllProducts();

        System.out.println("\n--- Product Catalog ---");
        for (Product product : products) {
            System.out.printf("ID: %d | Name: %s | Price: $%.2f | Stock: %d\n",
                    product.getProductId(), product.getName(), product.getPrice(), product.getQuantity());
            System.out.println("Description: " + product.getDescription());
            System.out.println("----------------------------------------");
        }

        System.out.println("\n1. Add to Cart");
        System.out.println("2. Back to Menu");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        if (choice == 1) {
            System.out.print("Enter Product ID to add to cart: ");
            int productId = scanner.nextInt();

            System.out.print("Enter Quantity: ");
            int quantity = scanner.nextInt();
            scanner.nextLine(); // consume newline

            Product product = productDAO.getProductById(productId);
            if (product != null && product.getQuantity() != null && ((Integer) product.getQuantity()) >= quantity) {
                boolean success = cartDAO.addToCart(currentUser.getUserId(), productId, quantity);
                if (success) {
                    System.out.println("Product added to cart successfully!");
                } else {
                    System.out.println("Failed to add product to cart.");
                }
            } else {
                System.out.println("Invalid product ID or insufficient stock.");
            }
        }
    }

    private static void viewCart() {
        List<CartItem> cartItems = cartDAO.getCartItems(currentUser.getUserId());

        if (cartItems.isEmpty()) {
            System.out.println("\nYour cart is empty.");
            return;
        }

        System.out.println("\n--- Your Shopping Cart ---");
        double total = 0;
        for (CartItem item : cartItems) {
            double itemTotal = item.getTotalPrice();
            System.out.printf("Cart ID: %d | Product: %s | Price: $%.2f | Qty: %d | Total: $%.2f\n",
                    item.getCartId(), item.getProductName(), item.getPrice(), item.getQuantity(), itemTotal);
            total += itemTotal;
        }
        System.out.printf("Cart Total: $%.2f\n", total);

        System.out.println("\n1. Remove Item");
        System.out.println("2. Checkout");
        System.out.println("3. Back to Menu");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (choice) {
            case 1:
                System.out.print("Enter Cart ID to remove: ");
                int cartId = scanner.nextInt();
                scanner.nextLine(); // consume newline

                boolean success = cartDAO.removeFromCart(cartId);
                if (success) {
                    System.out.println("Item removed from cart.");
                } else {
                    System.out.println("Failed to remove item.");
                }
                break;
            case 2:
                checkout();
                break;
            case 3:
                // Back to menu
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void checkout() {
        List<CartItem> cartItems = cartDAO.getCartItems(currentUser.getUserId());

        if (cartItems.isEmpty()) {
            System.out.println("Your cart is empty. Nothing to checkout.");
            return;
        }

        // Calculate total
        double total = cartItems.stream().mapToDouble(CartItem::getTotalPrice).sum();

        // Create order
        int orderId = orderDAO.createOrder(currentUser.getUserId(), total);
        if (orderId == -1) {
            System.out.println("Failed to create order.");
            return;
        }

        // Add order items
        boolean allItemsAdded = true;
        for (CartItem item : cartItems) {
            boolean success = orderDAO.addOrderItem(orderId, item.getProductId(), item.getQuantity(), item.getPrice());
            if (!success) {
                allItemsAdded = false;
                break;
            }

            // Update product quantity
            productDAO.updateProductQuantity(item.getProductId(), item.getQuantity());
        }

        if (allItemsAdded) {
            // Clear cart
            cartDAO.clearCart(currentUser.getUserId());
            System.out.println("Order placed successfully! Order ID: " + orderId);
        } else {
            System.out.println("Failed to place order. Some items couldn't be processed.");
        }
    }

    private static void viewOrders() {
        List<Order> orders = orderDAO.getUserOrders(currentUser.getUserId());

        if (orders.isEmpty()) {
            System.out.println("\nYou have no orders yet.");
            return;
        }

        System.out.println("\n--- Your Orders ---");
        for (Order order : orders) {
            System.out.printf("Order ID: %d | Date: %s | Total: $%.2f | Status: %s\n",
                    order.getOrderId(), order.getOrderDate(), order.getTotalAmount(), order.getStatus());

            List<OrderItem> orderItems = orderDAO.getOrderItems(order.getOrderId());
            for (OrderItem item : orderItems) {
                System.out.printf("  - %s | Qty: %d | Price: $%.2f | Total: $%.2f\n",
                        item.getProductName(), item.getQuantity(), item.getPrice(), item.getTotalPrice());
            }
            System.out.println("----------------------------------------");
        }
    }
}