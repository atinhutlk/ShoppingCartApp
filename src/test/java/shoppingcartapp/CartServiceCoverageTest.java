package shoppingcartapp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("CartService Coverage Tests")
class CartServiceCoverageTest {

    @Test
    @DisplayName("saveCartRecord commits when inserts succeed")
    void testSaveCartRecordCommitPath() throws Exception {
        JdbcTestDoubles.State state = new JdbcTestDoubles.State();
        CartService service = new CartService();
        List<CartItemEntry> items = List.of(
                new CartItemEntry(1, 10.0, 2, 20.0),
                new CartItemEntry(2, 5.0, 1, 5.0)
        );

        JdbcTestDoubles.withIsolatedFakeDriver(
                JdbcTestDoubles.Mode.SUCCESS,
                state,
                () -> {
                    service.saveCartRecord(3, 25.0, "en_US", items);
                    return null;
                }
        );

        assertTrue(state.autoCommitDisabled);
        assertTrue(state.commitCalled);
        assertFalse(state.rollbackCalled);
        assertEquals(2, state.addBatchCalls);
        assertEquals(3, state.totalItemsValue);
        assertEquals(25.0, state.totalCostValue, 0.001);
        assertEquals("en_US", state.languageValue);
    }

    @Test
    @DisplayName("saveCartRecord rolls back when record insert fails")
    void testSaveCartRecordRollbackOnInsertFailure() {
        JdbcTestDoubles.State state = new JdbcTestDoubles.State();
        CartService service = new CartService();
        List<CartItemEntry> items = List.of(new CartItemEntry(1, 10.0, 1, 10.0));

        assertThrows(SQLException.class, () -> JdbcTestDoubles.withIsolatedFakeDriver(
                JdbcTestDoubles.Mode.FAIL_RECORD_UPDATE,
                state,
                () -> {
                    service.saveCartRecord(1, 10.0, "fi_FI", items);
                    return null;
                }
        ));

        assertFalse(state.commitCalled);
        assertTrue(state.rollbackCalled);
    }

    @Test
    @DisplayName("saveCartRecord rolls back when generated key is missing")
    void testSaveCartRecordRollbackOnMissingGeneratedKey() {
        JdbcTestDoubles.State state = new JdbcTestDoubles.State();
        CartService service = new CartService();
        List<CartItemEntry> items = List.of(new CartItemEntry(1, 3.0, 1, 3.0));

        SQLException exception = assertThrows(SQLException.class, () -> JdbcTestDoubles.withIsolatedFakeDriver(
                JdbcTestDoubles.Mode.NO_GENERATED_KEY,
                state,
                () -> {
                    service.saveCartRecord(1, 3.0, "sv_SE", items);
                    return null;
                }
        ));

        assertTrue(exception.getMessage().contains("generated cart record id"));
        assertFalse(state.commitCalled);
        assertTrue(state.rollbackCalled);
    }
}

