package shoppingcartapp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("DatabaseConnection Coverage Tests")
class DatabaseConnectionCoverageTest {

    @Test
    @DisplayName("getConnection uses registered driver and returns a connection")
    void testGetConnectionWithFakeDriver() throws Exception {
        JdbcTestDoubles.State state = new JdbcTestDoubles.State();

        Connection connection = JdbcTestDoubles.withIsolatedFakeDriver(
                JdbcTestDoubles.Mode.SUCCESS,
                state,
                DatabaseConnection::getConnection
        );

        assertNotNull(connection);
        assertTrue(state.connectCalls > 0);
        connection.close();
    }

    @Test
    @DisplayName("Private constructor is covered via reflection")
    void testPrivateConstructorViaReflection() throws Exception {
        Constructor<DatabaseConnection> constructor = DatabaseConnection.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));

        constructor.setAccessible(true);
        DatabaseConnection instance = constructor.newInstance();
        assertNotNull(instance);
        assertEquals(DatabaseConnection.class, instance.getClass());
    }
}

