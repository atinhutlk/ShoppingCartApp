package shoppingcartapp;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

final class JdbcTestDoubles {

    enum Mode {
        SUCCESS,
        NO_GENERATED_KEY,
        FAIL_RECORD_UPDATE
    }

    static final class State {
        int connectCalls;
        boolean autoCommitDisabled;
        boolean commitCalled;
        boolean rollbackCalled;
        int addBatchCalls;
        int generatedId = 42;
        int totalItemsValue;
        double totalCostValue;
        String languageValue;
    }

    @FunctionalInterface
    interface CheckedSupplier<T> {
        T get() throws Exception;
    }

    private JdbcTestDoubles() {
    }

    static <T> T withIsolatedFakeDriver(Mode mode, State state, CheckedSupplier<T> action) throws Exception {
        // Temporarily replace registered JDBC drivers so tests never touch a real database.
        List<Driver> originalDrivers = Collections.list(DriverManager.getDrivers());
        for (Driver driver : originalDrivers) {
            DriverManager.deregisterDriver(driver);
        }

        Driver fakeDriver = new FakeDriver(mode, state);
        DriverManager.registerDriver(fakeDriver);
        try {
            return action.get();
        } finally {
            // Always restore original drivers to avoid leaking test state across classes.
            DriverManager.deregisterDriver(fakeDriver);
            for (Driver driver : originalDrivers) {
                DriverManager.registerDriver(driver);
            }
        }
    }

    private static final class FakeDriver implements Driver {
        private final Mode mode;
        private final State state;

        private FakeDriver(Mode mode, State state) {
            this.mode = mode;
            this.state = state;
        }

        @Override
        public Connection connect(String url, Properties info) throws SQLException {
            if (!acceptsURL(url)) {
                return null;
            }
            state.connectCalls++;
            return createConnectionProxy(mode, state);
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

    private static Connection createConnectionProxy(Mode mode, State state) {
        InvocationHandler handler = (proxy, method, args) -> {
            String methodName = method.getName();
            switch (methodName) {
                case "setAutoCommit":
                    if (args != null && args.length == 1 && args[0] instanceof Boolean) {
                        state.autoCommitDisabled = !((Boolean) args[0]);
                    }
                    return null;
                case "prepareStatement":
                    // Route SQL to the expected statement proxy used by CartService.
                    String sql = (String) args[0];
                    if (sql.contains("cart_records")) {
                        return createRecordPreparedStatementProxy(mode, state);
                    }
                    if (sql.contains("cart_items")) {
                        return createItemPreparedStatementProxy(state);
                    }
                    throw new SQLException("Unexpected SQL in test double: " + sql);
                case "commit":
                    state.commitCalled = true;
                    return null;
                case "rollback":
                    state.rollbackCalled = true;
                    return null;
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

    private static PreparedStatement createRecordPreparedStatementProxy(Mode mode, State state) {
        InvocationHandler handler = (proxy, method, args) -> {
            String methodName = method.getName();
            switch (methodName) {
                case "setInt":
                    if ((Integer) args[0] == 1) {
                        state.totalItemsValue = (Integer) args[1];
                    }
                    return null;
                case "setDouble":
                    if ((Integer) args[0] == 2) {
                        state.totalCostValue = (Double) args[1];
                    }
                    return null;
                case "setString":
                    if ((Integer) args[0] == 3) {
                        state.languageValue = (String) args[1];
                    }
                    return null;
                case "executeUpdate":
                    if (mode == Mode.FAIL_RECORD_UPDATE) {
                        throw new SQLException("Simulated insert failure");
                    }
                    return 1;
                case "getGeneratedKeys":
                    return createGeneratedKeysResultSetProxy(mode, state);
                case "close":
                    return null;
                default:
                    return defaultValue(method.getReturnType());
            }
        };

        return (PreparedStatement) Proxy.newProxyInstance(
                PreparedStatement.class.getClassLoader(),
                new Class<?>[]{PreparedStatement.class},
                handler
        );
    }

    private static PreparedStatement createItemPreparedStatementProxy(State state) {
        InvocationHandler handler = (proxy, method, args) -> {
            String methodName = method.getName();
            switch (methodName) {
                case "setInt", "setDouble", "setString":
                    return null;
                case "addBatch":
                    state.addBatchCalls++;
                    return null;
                case "executeBatch":
                    int size = Math.max(state.addBatchCalls, 1);
                    int[] results = new int[size];
                    for (int i = 0; i < size; i++) {
                        results[i] = Statement.SUCCESS_NO_INFO;
                    }
                    return results;
                case "close":
                    return null;
                default:
                    return defaultValue(method.getReturnType());
            }
        };

        return (PreparedStatement) Proxy.newProxyInstance(
                PreparedStatement.class.getClassLoader(),
                new Class<?>[]{PreparedStatement.class},
                handler
        );
    }

    private static ResultSet createGeneratedKeysResultSetProxy(Mode mode, State state) {
        List<Boolean> rowFlags = new ArrayList<>();
        if (mode != Mode.NO_GENERATED_KEY) {
            rowFlags.add(Boolean.TRUE);
        }

        InvocationHandler handler = (proxy, method, args) -> {
            String methodName = method.getName();
            switch (methodName) {
                case "next":
                    if (rowFlags.isEmpty()) {
                        return false;
                    }
                    rowFlags.remove(0);
                    return true;
                case "getInt":
                    return state.generatedId;
                case "close":
                    return null;
                default:
                    return defaultValue(method.getReturnType());
            }
        };

        return (ResultSet) Proxy.newProxyInstance(
                ResultSet.class.getClassLoader(),
                new Class<?>[]{ResultSet.class},
                handler
        );
    }

    private static Object defaultValue(Class<?> returnType) {
        // Safe defaults for proxy methods we do not explicitly simulate.
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
