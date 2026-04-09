package shoppingcartapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class LocalizationService {

    public Map<String, String> getLocalizedStrings(String language) throws SQLException {
        String sql = "SELECT `key`, value FROM localization_strings WHERE language = ?";
        Map<String, String> localizedStrings = new HashMap<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, language);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    localizedStrings.put(resultSet.getString("key"), resultSet.getString("value"));
                }
            }
        }

        return localizedStrings;
    }
}

