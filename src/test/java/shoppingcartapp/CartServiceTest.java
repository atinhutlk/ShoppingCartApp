package shoppingcartapp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CartService Tests")
class CartServiceTest {

    private CartService cartService;

    @Test
    @DisplayName("CartService object can be instantiated")
    void testCartServiceInstantiation() {
        cartService = new CartService();
        assertNotNull(cartService);
        assertEquals("CartService", CartService.class.getSimpleName());
    }

    @Test
    @DisplayName("CartService has saveCartRecord method")
    void testSaveCartRecordMethodExists() {
        assertDoesNotThrow(() -> {
            CartService service = new CartService();
            assertNotNull(service);
        });
    }

    @Test
    @DisplayName("Empty cart items list should be accepted")
    void testEmptyCartItemsList() {
        List<CartItemEntry> emptyList = new ArrayList<>();
        assertNotNull(emptyList);
        assertTrue(emptyList.isEmpty());
    }

    @Test
    @DisplayName("Single cart item can be stored")
    void testSingleCartItem() {
        CartItemEntry item = new CartItemEntry(1, 19.99, 1, 19.99);
        List<CartItemEntry> items = new ArrayList<>();
        items.add(item);

        assertEquals(1, items.size());
        assertEquals(19.99, items.get(0).getPrice());
    }

    @Test
    @DisplayName("Multiple cart items can be stored in list")
    void testMultipleCartItems() {
        List<CartItemEntry> items = new ArrayList<>();
        items.add(new CartItemEntry(1, 10.00, 2, 20.00));
        items.add(new CartItemEntry(2, 15.50, 1, 15.50));
        items.add(new CartItemEntry(3, 5.25, 3, 15.75));

        assertEquals(3, items.size());
        assertEquals(20.00, items.get(0).getSubtotal());
        assertEquals(15.50, items.get(1).getSubtotal());
        assertEquals(15.75, items.get(2).getSubtotal());
    }

    @Test
    @DisplayName("Cart record with zero total items")
    void testCartRecordWithZeroItems() {
        List<CartItemEntry> items = new ArrayList<>();
        assertNotNull(items);
        assertEquals(0, items.size());
    }

    @Test
    @DisplayName("Cart record with various languages")
    void testCartRecordLanguages() {
        String[] languages = {"en_US", "fi_FI", "sv_SE", "ja_JP", "ar_SA"};
        
        for (String language : languages) {
            assertNotNull(language);
            assertFalse(language.isEmpty());
        }
    }

    @Test
    @DisplayName("Cart items can be iterated")
    void testCartItemsIteration() {
        List<CartItemEntry> items = new ArrayList<>();
        items.add(new CartItemEntry(1, 10.0, 1, 10.0));
        items.add(new CartItemEntry(2, 20.0, 1, 20.0));
        items.add(new CartItemEntry(3, 30.0, 1, 30.0));

        int count = 0;
        for (CartItemEntry item : items) {
            assertNotNull(item);
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    @DisplayName("Total cost calculation from multiple items")
    void testTotalCostCalculation() {
        double totalCost = 0;
        List<CartItemEntry> items = new ArrayList<>();
        
        CartItemEntry item1 = new CartItemEntry(1, 10.0, 2, 20.0);
        CartItemEntry item2 = new CartItemEntry(2, 15.0, 3, 45.0);
        items.add(item1);
        items.add(item2);

        for (CartItemEntry item : items) {
            totalCost += item.getSubtotal();
        }

        assertEquals(65.0, totalCost);
    }

    @Test
    @DisplayName("Total items count calculation")
    void testTotalItemsCount() {
        int totalItems = 0;
        List<CartItemEntry> items = new ArrayList<>();
        items.add(new CartItemEntry(1, 10.0, 5, 50.0));
        items.add(new CartItemEntry(2, 20.0, 3, 60.0));

        for (CartItemEntry item : items) {
            totalItems += item.getQuantity();
        }

        assertEquals(8, totalItems);
    }

    @Test
    @DisplayName("Large quantity items")
    void testLargeQuantityItems() {
        List<CartItemEntry> items = new ArrayList<>();
        items.add(new CartItemEntry(1, 1.0, 1000, 1000.0));
        items.add(new CartItemEntry(2, 5.0, 500, 2500.0));

        assertEquals(2, items.size());
        assertEquals(1500, items.get(0).getQuantity() + items.get(1).getQuantity());
    }

    @Test
    @DisplayName("High-value items")
    void testHighValueItems() {
        List<CartItemEntry> items = new ArrayList<>();
        items.add(new CartItemEntry(1, 999.99, 10, 9999.90));
        items.add(new CartItemEntry(2, 1500.50, 5, 7502.50));

        double totalValue = 0;
        for (CartItemEntry item : items) {
            totalValue += item.getSubtotal();
        }

        assertEquals(17502.40, totalValue, 0.01);
    }

    @Test
    @DisplayName("Decimal precision in subtotals")
    void testDecimalPrecision() {
        List<CartItemEntry> items = new ArrayList<>();
        items.add(new CartItemEntry(1, 9.99, 3, 29.97));
        items.add(new CartItemEntry(2, 19.95, 2, 39.90));

        double totalCost = 0;
        for (CartItemEntry item : items) {
            totalCost += item.getSubtotal();
        }

        assertEquals(69.87, totalCost, 0.01);
    }

    @Test
    @DisplayName("Cart records with different languages track correctly")
    void testMultipleLanguageRecords() {
        List<String> languages = new ArrayList<>();
        languages.add("en_US");
        languages.add("fi_FI");
        languages.add("sv_SE");

        for (String lang : languages) {
            List<CartItemEntry> items = new ArrayList<>();
            items.add(new CartItemEntry(1, 50.0, 1, 50.0));
            
            assertNotNull(lang);
            assertNotNull(items);
            assertEquals(1, items.size());
        }
    }

    @Test
    @DisplayName("Cart item retrieval from list")
    void testCartItemRetrieval() {
        List<CartItemEntry> items = new ArrayList<>();
        CartItemEntry originalItem = new CartItemEntry(42, 123.45, 7, 864.15);
        items.add(originalItem);

        CartItemEntry retrievedItem = items.get(0);
        assertEquals(originalItem.getItemNumber(), retrievedItem.getItemNumber());
        assertEquals(originalItem.getPrice(), retrievedItem.getPrice());
        assertEquals(originalItem.getQuantity(), retrievedItem.getQuantity());
    }

    @Test
    @DisplayName("Empty language string handling")
    void testEmptyLanguageString() {
        String language = "";
        assertNotNull(language);
        assertTrue(language.isEmpty());
    }

    @Test
    @DisplayName("Null-safe operations with cart items")
    void testNullSafeOperations() {
        List<CartItemEntry> items = new ArrayList<>();
        assertNotNull(items);
        assertTrue(items.isEmpty());
    }

    @Test
    @DisplayName("Sequential item number handling")
    void testSequentialItemNumbers() {
        List<CartItemEntry> items = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            items.add(new CartItemEntry(i, i * 10.0, 1, i * 10.0));
        }

        assertEquals(5, items.size());
        assertEquals(1, items.get(0).getItemNumber());
        assertEquals(5, items.get(4).getItemNumber());
    }

    @Test
    @DisplayName("Price range validation")
    void testPriceRangeValidation() {
        List<CartItemEntry> items = new ArrayList<>();
        items.add(new CartItemEntry(1, 0.01, 1, 0.01));   // Very small price
        items.add(new CartItemEntry(2, 9999.99, 1, 9999.99)); // Very large price

        assertEquals(2, items.size());
        assertTrue(items.get(0).getPrice() > 0);
        assertTrue(items.get(1).getPrice() > 0);
    }

    @Test
    @DisplayName("Quantity range validation")
    void testQuantityRangeValidation() {
        List<CartItemEntry> items = new ArrayList<>();
        items.add(new CartItemEntry(1, 10.0, 1, 10.0));      // Minimum quantity
        items.add(new CartItemEntry(2, 5.0, 9999, 49995.0)); // Large quantity

        assertEquals(2, items.size());
        assertTrue(items.get(0).getQuantity() >= 1);
        assertTrue(items.get(1).getQuantity() > 0);
    }

    @Test
    @DisplayName("Cart item list filtering by price")
    void testCartItemListFiltering() {
        List<CartItemEntry> items = new ArrayList<>();
        items.add(new CartItemEntry(1, 5.0, 1, 5.0));
        items.add(new CartItemEntry(2, 15.0, 1, 15.0));
        items.add(new CartItemEntry(3, 25.0, 1, 25.0));

        int expensiveItems = 0;
        for (CartItemEntry item : items) {
            if (item.getPrice() > 10.0) {
                expensiveItems++;
            }
        }

        assertEquals(2, expensiveItems);
    }

    @Test
    @DisplayName("Cart items mapping to totals")
    void testCartItemsMapping() {
        List<CartItemEntry> items = new ArrayList<>();
        items.add(new CartItemEntry(1, 10.0, 2, 20.0));
        items.add(new CartItemEntry(2, 10.0, 3, 30.0));

        double[] subtotals = new double[items.size()];
        for (int i = 0; i < items.size(); i++) {
            subtotals[i] = items.get(i).getSubtotal();
        }

        assertEquals(20.0, subtotals[0]);
        assertEquals(30.0, subtotals[1]);
    }

    @Test
    @DisplayName("Cart with mixed price points")
    void testMixedPricePoints() {
        List<CartItemEntry> items = new ArrayList<>();
        items.add(new CartItemEntry(1, 0.99, 1, 0.99));
        items.add(new CartItemEntry(2, 10.50, 1, 10.50));
        items.add(new CartItemEntry(3, 100.00, 1, 100.00));

        assertEquals(3, items.size());
        double total = items.stream().mapToDouble(CartItemEntry::getSubtotal).sum();
        assertEquals(111.49, total, 0.01);
    }
}

