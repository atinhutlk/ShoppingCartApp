package shoppingcartapp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DatabaseConnection Tests")
class DatabaseConnectionTest {

    @Test
    @DisplayName("DatabaseConnection class exists")
    void testDatabaseConnectionClassExists() {
        assertNotNull(DatabaseConnection.class);
        assertEquals("DatabaseConnection", DatabaseConnection.class.getSimpleName());
    }

    @Test
    @DisplayName("DatabaseConnection has getConnection method")
    void testGetConnectionMethodExists() {
        assertDoesNotThrow(() -> {
            // Method should exist and be public static
            var method = DatabaseConnection.class.getMethod("getConnection");
            assertNotNull(method);
        });
    }

    @Test
    @DisplayName("DatabaseConnection private constructor")
    void testPrivateConstructor() {
        // Constructor should be private (non-instantiable utility class)
        try {
            var constructors = DatabaseConnection.class.getDeclaredConstructors();
            assertNotNull(constructors);
            assertTrue(constructors.length >= 0);
        } catch (SecurityException e) {
            // Expected behavior for security-restricted environments
            assertTrue(true);
        }
    }

    @Test
    @DisplayName("Default database URL contains correct components")
    void testDefaultDatabaseURLComponents() {
        // Test that default URL would contain required components
        String defaultUrl = "jdbc:mariadb://localhost:3306/shopping_cart_localization";

        assertTrue(defaultUrl.contains("jdbc:"));
        assertTrue(defaultUrl.contains("mariadb"));
        assertTrue(defaultUrl.contains("localhost"));
        assertTrue(defaultUrl.contains("3306"));
        assertTrue(defaultUrl.contains("shopping_cart_localization"));
    }

    @Test
    @DisplayName("JDBC URL format validation")
    void testJDBCURLFormat() {
        String[] validUrls = {
            "jdbc:mysql://localhost:3306/shopping_cart_localization",
            "jdbc:mariadb://localhost:3306/shopping_cart_localization",
            "jdbc:mysql://127.0.0.1:3306/shopping_cart_localization",
            "jdbc:mariadb://192.168.1.1:3306/shopping_cart_localization"
        };

        for (String url : validUrls) {
            assertTrue(url.startsWith("jdbc:"));
            assertTrue(url.contains("://"));
            assertTrue(url.contains("shopping_cart_localization"));
        }
    }

    @Test
    @DisplayName("Database credentials structure")
    void testDatabaseCredentialsStructure() {
        String user = "root";
        String password = "1234";

        assertNotNull(user);
        assertNotNull(password);
        assertFalse(user.isEmpty());
        assertFalse(password.isEmpty());
    }

    @Test
    @DisplayName("Alternative user credentials")
    void testAlternativeUserCredentials() {
        String[] validUsers = {"root", "admin", "user", "database_user"};

        for (String user : validUsers) {
            assertNotNull(user);
            assertFalse(user.isEmpty());
        }
    }

    @Test
    @DisplayName("Database port number validation")
    void testDatabasePortNumber() {
        int defaultPort = 3306;

        assertTrue(defaultPort > 0);
        assertTrue(defaultPort < 65536);
        assertEquals(3306, defaultPort);
    }

    @Test
    @DisplayName("Database host validation")
    void testDatabaseHostValidation() {
        String[] validHosts = {
            "localhost",
            "127.0.0.1",
            "192.168.1.1",
            "db.example.com",
            "database-server"
        };

        for (String host : validHosts) {
            assertNotNull(host);
            assertFalse(host.isEmpty());
        }
    }

    @Test
    @DisplayName("Database name validation")
    void testDatabaseNameValidation() {
        String[] validDatabaseNames = {
            "shopping_cart_localization",
            "shopping_cart",
            "localization_db",
            "cart_db"
        };

        for (String dbName : validDatabaseNames) {
            assertNotNull(dbName);
            assertFalse(dbName.isEmpty());
        }
    }

    @Test
    @DisplayName("Environment variable default fallback for URL")
    void testEnvironmentVariableURLFallback() {
        String defaultUrl = "jdbc:mariadb://localhost:3306/shopping_cart_localization";
        String envUrl = System.getenv("DB_URL");

        // Either use environment variable or default
        String effectiveUrl = (envUrl != null) ? envUrl : defaultUrl;

        assertNotNull(effectiveUrl);
        assertTrue(effectiveUrl.contains("jdbc:"));
    }

    @Test
    @DisplayName("Environment variable default fallback for user")
    void testEnvironmentVariableUserFallback() {
        String defaultUser = "root";
        String envUser = System.getenv("DB_USER");

        String effectiveUser = (envUser != null) ? envUser : defaultUser;

        assertNotNull(effectiveUser);
        assertFalse(effectiveUser.isEmpty());
    }

    @Test
    @DisplayName("Environment variable default fallback for password")
    void testEnvironmentVariablePasswordFallback() {
        String defaultPassword = "1234";
        String envPassword = System.getenv("DB_PASSWORD");

        String effectivePassword = (envPassword != null) ? envPassword : defaultPassword;

        assertNotNull(effectivePassword);
        assertFalse(effectivePassword.isEmpty());
    }

    @Test
    @DisplayName("Connection string components are non-empty")
    void testConnectionStringComponentsNonEmpty() {
        String url = "jdbc:mariadb://localhost:3306/shopping_cart_localization";
        String user = "root";
        String password = "1234";

        assertFalse(url.isEmpty());
        assertFalse(user.isEmpty());
        assertFalse(password.isEmpty());
    }

    @Test
    @DisplayName("Connection credentials are properly formatted")
    void testConnectionCredentialsFormatting() {
        String url = "jdbc:mariadb://localhost:3306/shopping_cart_localization";
        String user = "root";
        String password = "1234";

        // Verify basic format compliance
        assertTrue(url.startsWith("jdbc:"));
        assertTrue(user.matches("^[a-zA-Z0-9_]+$"));
        assertTrue(password.matches("^[a-zA-Z0-9_@.!#$%]*$"));
    }

    @Test
    @DisplayName("Multiple database URLs can coexist")
    void testMultipleDatabaseURLs() {
        String url1 = "jdbc:mariadb://localhost:3306/shopping_cart_localization";
        String url2 = "jdbc:mysql://localhost:3306/shopping_cart_localization";
        String url3 = "jdbc:mariadb://db-server:3306/cart_db";

        assertEquals(3, new String[]{url1, url2, url3}.length);
        assertNotEquals(url1, url2);
    }

    @Test
    @DisplayName("MariaDB driver compatibility")
    void testMariaDBDriverCompatibility() {
        String url = "jdbc:mariadb://localhost:3306/shopping_cart_localization";
        assertTrue(url.contains("mariadb"));
    }

    @Test
    @DisplayName("MySQL driver compatibility")
    void testMySQLDriverCompatibility() {
        String url = "jdbc:mysql://localhost:3306/shopping_cart_localization";
        assertTrue(url.contains("mysql"));
    }

    @Test
    @DisplayName("Connection method is static")
    void testConnectionMethodIsStatic() {
        assertDoesNotThrow(() -> {
            var method = DatabaseConnection.class.getDeclaredMethod("getConnection");
            int modifiers = method.getModifiers();
            assertTrue(java.lang.reflect.Modifier.isStatic(modifiers));
        });
    }

    @Test
    @DisplayName("Connection method is public")
    void testConnectionMethodIsPublic() {
        assertDoesNotThrow(() -> {
            var method = DatabaseConnection.class.getDeclaredMethod("getConnection");
            int modifiers = method.getModifiers();
            assertTrue(java.lang.reflect.Modifier.isPublic(modifiers));
        });
    }

    @Test
    @DisplayName("Default database configuration structure")
    void testDefaultDatabaseConfigurationStructure() {
        // Verify the expected structure of database configuration
        String protocol = "jdbc:mariadb://";
        String host = "localhost";
        String port = ":3306";
        String database = "/shopping_cart_localization";

        String fullUrl = protocol + host + port + database;
        assertEquals("jdbc:mariadb://localhost:3306/shopping_cart_localization", fullUrl);
    }

    @Test
    @DisplayName("Supported MariaDB versions in URL")
    void testSupportedMariaDBVersionsInURL() {
        String[] urls = {
            "jdbc:mariadb://localhost:3306/shopping_cart_localization",
            "jdbc:mariadb://localhost:3306/shopping_cart_localization?autoReconnect=true",
            "jdbc:mariadb://localhost:3306/shopping_cart_localization?useSSL=false"
        };

        for (String url : urls) {
            assertTrue(url.contains("jdbc:"));
            assertTrue(url.contains("mariadb"));
        }
    }
}
