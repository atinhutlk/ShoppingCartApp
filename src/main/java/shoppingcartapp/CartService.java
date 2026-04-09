package shoppingcartapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CartService {

    public void saveCartRecord(int totalItems, double totalCost, String language, List<CartItemEntry> cartItems)
            throws SQLException {
        String insertRecordSql = "INSERT INTO cart_records (total_items, total_cost, language) VALUES (?, ?, ?)";
        String insertItemSql = "INSERT INTO cart_items (cart_record_id, item_number, price, quantity, subtotal) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement recordStatement = connection.prepareStatement(insertRecordSql,
                    Statement.RETURN_GENERATED_KEYS)) {
                recordStatement.setInt(1, totalItems);
                recordStatement.setDouble(2, totalCost);
                recordStatement.setString(3, language);
                recordStatement.executeUpdate();

                int cartRecordId = getGeneratedRecordId(recordStatement);

                try (PreparedStatement itemStatement = connection.prepareStatement(insertItemSql)) {
                    for (CartItemEntry item : cartItems) {
                        itemStatement.setInt(1, cartRecordId);
                        itemStatement.setInt(2, item.getItemNumber());
                        itemStatement.setDouble(3, item.getPrice());
                        itemStatement.setInt(4, item.getQuantity());
                        itemStatement.setDouble(5, item.getSubtotal());
                        itemStatement.addBatch();
                    }
                    itemStatement.executeBatch();
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }
    }

    private int getGeneratedRecordId(PreparedStatement statement) throws SQLException {
        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
        }
        throw new SQLException("Failed to get generated cart record id.");
    }
}

