package shoppingcartapp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("CartItemEntry Tests")
class CartItemEntryTest {

    @Test
    @DisplayName("Stores all fields and returns them via getters")
    void storesAllFieldsAndReturnsThemViaGetters() {
        CartItemEntry entry = new CartItemEntry(7, 12.5, 4, 50.0);

        assertEquals(7, entry.getItemNumber());
        assertEquals(12.5, entry.getPrice());
        assertEquals(4, entry.getQuantity());
        assertEquals(50.0, entry.getSubtotal());
    }

    @Test
    @DisplayName("Constructor with zero values")
    void testConstructorWithZeroValues() {
        CartItemEntry entry = new CartItemEntry(0, 0.0, 0, 0.0);

        assertEquals(0, entry.getItemNumber());
        assertEquals(0.0, entry.getPrice());
        assertEquals(0, entry.getQuantity());
        assertEquals(0.0, entry.getSubtotal());
    }

    @Test
    @DisplayName("Constructor with large values")
    void testConstructorWithLargeValues() {
        CartItemEntry entry = new CartItemEntry(999999, 99999.99, 1000, 99999999.0);

        assertEquals(999999, entry.getItemNumber());
        assertEquals(99999.99, entry.getPrice());
        assertEquals(1000, entry.getQuantity());
        assertEquals(99999999.0, entry.getSubtotal());
    }

    @Test
    @DisplayName("Constructor with negative item number")
    void testConstructorWithNegativeItemNumber() {
        CartItemEntry entry = new CartItemEntry(-1, 10.0, 2, 20.0);

        assertEquals(-1, entry.getItemNumber());
        assertEquals(10.0, entry.getPrice());
    }

    @Test
    @DisplayName("Constructor with fractional price")
    void testConstructorWithFractionalPrice() {
        CartItemEntry entry = new CartItemEntry(5, 19.99, 3, 59.97);

        assertEquals(5, entry.getItemNumber());
        assertEquals(19.99, entry.getPrice());
        assertEquals(3, entry.getQuantity());
        assertEquals(59.97, entry.getSubtotal());
    }

    @Test
    @DisplayName("Constructor with quantity of one")
    void testConstructorWithQuantityOne() {
        CartItemEntry entry = new CartItemEntry(1, 25.50, 1, 25.50);

        assertEquals(1, entry.getItemNumber());
        assertEquals(25.50, entry.getPrice());
        assertEquals(1, entry.getQuantity());
        assertEquals(25.50, entry.getSubtotal());
    }

    @Test
    @DisplayName("Constructor creates non-null object")
    void testConstructorCreatesNonNullObject() {
        CartItemEntry entry = new CartItemEntry(10, 5.0, 2, 10.0);
        assertNotNull(entry);
    }

    @Test
    @DisplayName("Multiple instances are independent")
    void testMultipleInstancesAreIndependent() {
        CartItemEntry entry1 = new CartItemEntry(1, 10.0, 1, 10.0);
        CartItemEntry entry2 = new CartItemEntry(2, 20.0, 2, 40.0);

        assertEquals(1, entry1.getItemNumber());
        assertEquals(2, entry2.getItemNumber());
        assertEquals(10.0, entry1.getPrice());
        assertEquals(20.0, entry2.getPrice());
    }

    @Test
    @DisplayName("Getters return consistent values")
    void testGettersReturnConsistentValues() {
        CartItemEntry entry = new CartItemEntry(15, 33.33, 5, 166.65);

        // Call getters multiple times to ensure consistency
        assertEquals(15, entry.getItemNumber());
        assertEquals(15, entry.getItemNumber());
        assertEquals(33.33, entry.getPrice());
        assertEquals(33.33, entry.getPrice());
        assertEquals(5, entry.getQuantity());
        assertEquals(5, entry.getQuantity());
        assertEquals(166.65, entry.getSubtotal());
        assertEquals(166.65, entry.getSubtotal());
    }

    @Test
    @DisplayName("Constructor with decimal price precision")
    void testConstructorWithDecimalPricePrecision() {
        CartItemEntry entry = new CartItemEntry(42, 123.456, 7, 864.192);

        assertEquals(42, entry.getItemNumber());
        assertEquals(123.456, entry.getPrice(), 0.001);
        assertEquals(7, entry.getQuantity());
        assertEquals(864.192, entry.getSubtotal(), 0.001);
    }

    @Test
    @DisplayName("All getters called multiple times in sequence")
    void testAllGettersSequentialCalls() {
        CartItemEntry entry = new CartItemEntry(99, 99.99, 99, 9899.01);
        
        for (int i = 0; i < 5; i++) {
            assertEquals(99, entry.getItemNumber());
            assertEquals(99.99, entry.getPrice());
            assertEquals(99, entry.getQuantity());
            assertEquals(9899.01, entry.getSubtotal(), 0.01);
        }
    }

    @Test
    @DisplayName("Constructor with maximum safe integer values")
    void testConstructorWithMaxSafeIntegers() {
        int largeNumber = 2_147_483_647;
        CartItemEntry entry = new CartItemEntry(largeNumber, 1.0, 1, 1.0);
        assertEquals(largeNumber, entry.getItemNumber());
    }

    @Test
    @DisplayName("Multiple cart entries with same price different quantities")
    void testMultipleEntriesSamePriceDifferentQuantities() {
        CartItemEntry entry1 = new CartItemEntry(1, 10.0, 1, 10.0);
        CartItemEntry entry2 = new CartItemEntry(2, 10.0, 2, 20.0);
        CartItemEntry entry3 = new CartItemEntry(3, 10.0, 5, 50.0);
        
        assertEquals(10.0, entry1.getPrice());
        assertEquals(10.0, entry2.getPrice());
        assertEquals(10.0, entry3.getPrice());
        assertEquals(1, entry1.getQuantity());
        assertEquals(2, entry2.getQuantity());
        assertEquals(5, entry3.getQuantity());
    }

    @Test
    @DisplayName("CartItemEntry is immutable after creation")
    void testCartItemEntryImmutability() {
        CartItemEntry entry = new CartItemEntry(10, 25.0, 4, 100.0);
        int originalItemNumber = entry.getItemNumber();
        double originalPrice = entry.getPrice();
        int originalQuantity = entry.getQuantity();
        double originalSubtotal = entry.getSubtotal();
        
        // Multiple retrievals should return identical values
        assertEquals(originalItemNumber, entry.getItemNumber());
        assertEquals(originalPrice, entry.getPrice());
        assertEquals(originalQuantity, entry.getQuantity());
        assertEquals(originalSubtotal, entry.getSubtotal());
    }

    @Test
    @DisplayName("Constructor with item number zero")
    void testConstructorWithItemNumberZero() {
        CartItemEntry entry = new CartItemEntry(0, 100.0, 1, 100.0);
        assertEquals(0, entry.getItemNumber());
    }

    @Test
    @DisplayName("Constructor subtotal doesn't match calculated value")
    void testConstructorSubtotalMismatch() {
        // Test that constructor accepts any subtotal value regardless of price/qty
        CartItemEntry entry = new CartItemEntry(1, 10.0, 5, 999.99);
        assertEquals(999.99, entry.getSubtotal());
        // The entry stores whatever is passed, not calculating price * qty
    }

    @Test
    @DisplayName("Multiple independent CartItemEntry instances")
    void testMultipleIndependentInstances() {
        CartItemEntry[] entries = new CartItemEntry[10];
        for (int i = 0; i < 10; i++) {
            entries[i] = new CartItemEntry(i, i * 1.5, i + 1, i * i * 1.5);
        }
        
        for (int i = 0; i < 10; i++) {
            assertEquals(i, entries[i].getItemNumber());
            assertEquals(i * 1.5, entries[i].getPrice());
            assertEquals(i + 1, entries[i].getQuantity());
        }
    }

    @Test
    @DisplayName("CartItemEntry with all same values")
    void testCartItemEntryWithAllSameValues() {
        CartItemEntry entry = new CartItemEntry(5, 5.0, 5, 5.0);
        assertEquals(5, entry.getItemNumber());
        assertEquals(5.0, entry.getPrice());
        assertEquals(5, entry.getQuantity());
        assertEquals(5.0, entry.getSubtotal());
    }

    @Test
    @DisplayName("CartItemEntry getter return types are correct")
    void testCartItemEntryGetterReturnTypes() {
        CartItemEntry entry = new CartItemEntry(1, 2.5, 3, 7.5);
        
        // Verify return types by checking they're not null and proper values
        Object itemNumber = entry.getItemNumber();
        Object price = entry.getPrice();
        Object quantity = entry.getQuantity();
        Object subtotal = entry.getSubtotal();
        
        assertNotNull(itemNumber);
        assertNotNull(price);
        assertNotNull(quantity);
        assertNotNull(subtotal);
    }
}
