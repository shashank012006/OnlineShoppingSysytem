package dao;

import dao.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Order;
import model.OrderItem;

public class OrderDAO {

    public int createOrderWithItems(int userId, List<OrderItem> orderItems, double totalAmount) {
        String orderSql = "INSERT INTO Orders (user_id, total_amount) VALUES (?, ?)";
        String orderItemSql = "INSERT INTO OrderItems (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        int orderId = -1;

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement orderStmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                orderStmt.setInt(1, userId);
                orderStmt.setDouble(2, totalAmount);
                int affectedRows = orderStmt.executeUpdate();

                if (affectedRows == 0) {
                    conn.rollback();
                    return -1;
                }

                try (ResultSet generatedKeys = orderStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        orderId = generatedKeys.getInt(1);
                    } else {
                        conn.rollback();
                        return -1;
                    }
                }
            }

            try (PreparedStatement orderItemStmt = conn.prepareStatement(orderItemSql)) {
                for (OrderItem item : orderItems) {
                    orderItemStmt.setInt(1, orderId);
                    orderItemStmt.setInt(2, item.getProductId());
                    orderItemStmt.setInt(3, item.getQuantity());
                    orderItemStmt.setDouble(4, item.getPrice());
                    orderItemStmt.addBatch();
                }
                int[] results = orderItemStmt.executeBatch();

                for (int res : results) {
                    if (res == Statement.EXECUTE_FAILED) {
                        conn.rollback();
                        return -1;
                    }
                }
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

        return orderId;
    }

    public List<Order> getUserOrders(int userId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE user_id = ? ORDER BY order_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                order.setUserId(rs.getInt("user_id"));
                order.setOrderDate(rs.getTimestamp("order_date"));
                order.setTotalAmount(rs.getDouble("total_amount"));
                order.setStatus(rs.getString("status"));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public List<OrderItem> getOrderItems(int orderId) {
        List<OrderItem> orderItems = new ArrayList<>();
        String sql = "SELECT oi.*, p.name FROM OrderItems oi JOIN Products p ON oi.product_id = p.product_id " +
                     "WHERE oi.order_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                OrderItem item = new OrderItem();
                item.setItemId(rs.getInt("item_id"));
                item.setOrderId(rs.getInt("order_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setProductName(rs.getString("name"));
                item.setQuantity(rs.getInt("quantity"));
                item.setPrice(rs.getDouble("price"));
                orderItems.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderItems;
    }
}
