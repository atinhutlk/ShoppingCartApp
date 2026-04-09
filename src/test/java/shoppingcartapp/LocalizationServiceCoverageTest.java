package shoppingcartapp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("LocalizationService Coverage Tests")
class LocalizationServiceCoverageTest {

    @Test
    @DisplayName("getLocalizedStrings returns populated map")
    void testGetLocalizedStringsWithRows() throws Exception {
        FakeLocalizationState state = new FakeLocalizationState();
        state.rows.add(new String[]{"ITEM_NAME", "Item"});
        state.rows.add(new String[]{"PRICE", "Price"});

        LocalizationService service = new LocalizationService();

        Map<String, String> result = withFakeLocalizationDriver(state, () -> service.getLocalizedStrings("en_US"));

        assertEquals("en_US", state.languageParam);
        assertEquals(2, result.size());
        assertEquals("Item", result.get("ITEM_NAME"));
        assertEquals("Price", result.get("PRICE"));
    }

    @Test
    @DisplayName("getLocalizedStrings returns empty map when no rows")
    void testGetLocalizedStringsWithNoRows() throws Exception {
        FakeLocalizationState state = new FakeLocalizationState();
        LocalizationService service = new LocalizationService();

        Map<String, String> result = withFakeLocalizationDriver(state, () -> service.getLocalizedStrings("fi_FI"));

        assertEquals("fi_FI", state.languageParam);
        assertTrue(result.isEmpty());
    }

    private static <T> T withFakeLocalizationDriver(FakeLocalizationState state, CheckedSupplier<T> action)
            throws Exception {
        List<Driver> originalDrivers = Collections.list(DriverManager.getDrivers());
        for (Driver driver : originalDrivers) {
            DriverManager.deregisterDriver(driver);
        }

        Driver fakeDriver = new FakeLocalizationDriver(state);
        DriverManager.registerDriver(fakeDriver);

        try {
            return action.get();
        } finally {
            DriverManager.deregisterDriver(fakeDriver);
            for (Driver driver : originalDrivers) {
                DriverManager.registerDriver(driver);
            }
        }
    }

    @FunctionalInterface
    private interface CheckedSupplier<T> {
        T get() throws Exception;
    }

    private static final class FakeLocalizationState {
        private final List<String[]> rows = new ArrayList<>();
        private String languageParam;
    }

    private static final class FakeLocalizationDriver implements Driver {
        private final FakeLocalizationState state;

        private FakeLocalizationDriver(FakeLocalizationState state) {
            this.state = state;
        }

        @Override
        public Connection connect(String url, Properties info) {
            if (!acceptsURL(url)) {
                return null;
            }
            return createConnectionProxy(state);
        }

        @Override
        public boolean acceptsURL(String url) {
            return url != null && url.startsWith("jdbc:");
        }

        @Override
        public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) {
            return new DriverPropertyInfo[0];
        }

        @Override
        public int getMajorVersion() {
            return 1;
        }

        @Override
        public int getMinorVersion() {
            return 0;
        }

        @Override
        public boolean jdbcCompliant() {
            return false;
        }

        @Override
        public Logger getParentLogger() throws SQLFeatureNotSupportedException {
            throw new SQLFeatureNotSupportedException("Not supported in fake driver");
        }
    }

    private static Connection createConnectionProxy(FakeLocalizationState state) {
        InvocationHandler handler = (proxy, method, args) -> {
            String name = method.getName();
            switch (name) {
                case "prepareStatement":
                    return createPreparedStatementProxy(state);
                case "close":
                    return null;
                case "isClosed":
                    return false;
                default:
                    return defaultValue(method.getReturnType());
            }
        };

        return (Connection) Proxy.newProxyInstance(
                Connection.class.getClassLoader(),
                new Class<?>[]{Connection.class},
                handler
        );
    }

    private static PreparedStatement createPreparedStatementProxy(FakeLocalizationState state) {
        InvocationHandler handler = (proxy, method, args) -> {
            String name = method.getName();
            return switch (name) {
                case "setString" -> {
                    state.languageParam = (String) args[1];
                    yield null;
                }
                case "executeQuery" -> createResultSetProxy(state.rows);
                case "close" -> null;
                default -> defaultValue(method.getReturnType());
            };
        };

        return (PreparedStatement) Proxy.newProxyInstance(
                PreparedStatement.class.getClassLoader(),
                new Class<?>[]{PreparedStatement.class},
                handler
        );
    }

    private static ResultSet createResultSetProxy(List<String[]> rows) {
        class Cursor {
            int index = -1;
        }
        Cursor cursor = new Cursor();

        InvocationHandler handler = (proxy, method, args) -> {
            String name = method.getName();
            return switch (name) {
                case "next" -> {
                    cursor.index++;
                    yield cursor.index < rows.size();
                }
                case "getString" -> {
                    String column = (String) args[0];
                    Map<String, String> rowMap = new HashMap<>();
                    rowMap.put("key", rows.get(cursor.index)[0]);
                    rowMap.put("value", rows.get(cursor.index)[1]);
                    yield rowMap.get(column);
                }
                case "close" -> null;
                default -> defaultValue(method.getReturnType());
            };
        };

        return (ResultSet) Proxy.newProxyInstance(
                ResultSet.class.getClassLoader(),
                new Class<?>[]{ResultSet.class},
                handler
        );
    }

    private static Object defaultValue(Class<?> returnType) {
        if (!returnType.isPrimitive()) {
            return null;
        }
        if (returnType == boolean.class) {
            return false;
        }
        if (returnType == byte.class) {
            return (byte) 0;
        }
        if (returnType == short.class) {
            return (short) 0;
        }
        if (returnType == int.class) {
            return 0;
        }
        if (returnType == long.class) {
            return 0L;
        }
        if (returnType == float.class) {
            return 0.0f;
        }
        if (returnType == double.class) {
            return 0.0d;
        }
        if (returnType == char.class) {
            return '\0';
        }
        return null;
    }
}
