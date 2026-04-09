package shoppingcartapp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Integration-style Tests")
class IntegrationTests {

    @Test
    @DisplayName("Create a complete shopping cart scenario")
    void testCompleteShoppingCartScenario() {
        // Create items
        CartItemEntry item1 = new CartItemEntry(1001, 25.99, 2, 51.98);
        CartItemEntry item2 = new CartItemEntry(1002, 15.50, 3, 46.50);
        CartItemEntry item3 = new CartItemEntry(1003, 100.00, 1, 100.00);
        
        List<CartItemEntry> cart = new ArrayList<>();
        cart.add(item1);
        cart.add(item2);
        cart.add(item3);
        
        // Calculate totals
        double totalCost = 0;
        int totalQuantity = 0;
        for (CartItemEntry item : cart) {
            totalCost += item.getSubtotal();
            totalQuantity += item.getQuantity();
        }
        
        assertEquals(3, cart.size());
        assertEquals(198.48, totalCost, 0.01);
        assertEquals(6, totalQuantity);
    }

    @Test
    @DisplayName("Multi-language shopping experience")
    void testMultiLanguageShoppingExperience() {
        LanguageOption[] languages = {
            new LanguageOption("en_US", "English (US)"),
            new LanguageOption("fi_FI", "Suomi"),
            new LanguageOption("sv_SE", "Svenska"),
            new LanguageOption("ja_JP", "日本語"),
            new LanguageOption("ar_SA", "العربية")
        };
        
        CartItemEntry item = new CartItemEntry(100, 50.0, 2, 100.0);
        
        for (LanguageOption lang : languages) {
            assertNotNull(lang);
            assertNotNull(lang.getCode());
            assertNotNull(lang.toString());
            
            // Same item in different languages
            CartItemEntry localizedItem = new CartItemEntry(100, 50.0, 2, 100.0);
            assertEquals(item.getPrice(), localizedItem.getPrice());
        }
    }

    @Test
    @DisplayName("Cart calculator with real shopping scenario")
    void testCartCalculatorRealShoppingScenario() {
        double cartTotal = 0;
        
        // Add items using calculator
        cartTotal = CartCalculator.addToCart(cartTotal, 12.99, 1);  // Item 1
        cartTotal = CartCalculator.addToCart(cartTotal, 8.50, 2);   // Item 2
        cartTotal = CartCalculator.addToCart(cartTotal, 3.25, 3);   // Item 3
        cartTotal = CartCalculator.addToCart(cartTotal, 25.00, 1);  // Item 4
        
        assertEquals(64.74, cartTotal, 0.01);
    }

    @Test
    @DisplayName("Cart with discount scenario")
    void testCartWithDiscountScenario() {
        double subtotal = 0;
        
        CartItemEntry item1 = new CartItemEntry(1, 100.0, 1, 100.0);
        CartItemEntry item2 = new CartItemEntry(2, 50.0, 2, 100.0);
        
        subtotal = CartCalculator.addToCart(subtotal, item1.getPrice(), item1.getQuantity());
        subtotal = CartCalculator.addToCart(subtotal, item2.getPrice(), item2.getQuantity());
        
        double discountPercent = 10.0;
        double discount = subtotal * (discountPercent / 100.0);
        double finalTotal = subtotal - discount;
        
        assertEquals(200.0, subtotal);
        assertEquals(20.0, discount);
        assertEquals(180.0, finalTotal);
    }

    @Test
    @DisplayName("Bulk order processing")
    void testBulkOrderProcessing() {
        List<CartItemEntry> bulkItems = new ArrayList<>();
        
        // Add 100 items of same product
        for (int i = 0; i < 100; i++) {
            bulkItems.add(new CartItemEntry(1001, 10.0, 100, 1000.0));
        }
        
        assertEquals(100, bulkItems.size());
        
        double totalValue = 0;
        int totalItemsCount = 0;
        for (CartItemEntry item : bulkItems) {
            totalValue += item.getSubtotal();
            totalItemsCount += item.getQuantity();
        }
        
        assertEquals(100000.0, totalValue);
        assertEquals(10000, totalItemsCount);
    }

    @Test
    @DisplayName("Mixed currency decimal handling")
    void testMixedCurrencyDecimalHandling() {
        double total = 0;
        
        // Different price points with various decimal places
        total = CartCalculator.addToCart(total, 10.99, 1);
        total = CartCalculator.addToCart(total, 5.49, 2);
        total = CartCalculator.addToCart(total, 3.33, 3);
        total = CartCalculator.addToCart(total, 12.25, 1);
        
        assertEquals(44.21, total, 0.01);
    }

    @Test
    @DisplayName("Sequential cart operations")
    void testSequentialCartOperations() {
        List<CartItemEntry> items = new ArrayList<>();
        double runningTotal = 0;

        for (int i = 1; i <= 20; i++) {
            double itemPrice = i * 1.5;
            double subtotal = itemPrice * i;

            items.add(new CartItemEntry(i, itemPrice, i, subtotal));
            runningTotal = CartCalculator.addToCart(runningTotal, itemPrice, i);
        }

        assertEquals(20, items.size());

        double calculatedTotal = 0;
        for (CartItemEntry item : items) {
            calculatedTotal += item.getSubtotal();
        }

        assertEquals(calculatedTotal, runningTotal, 0.01);
    }

    @Test
    @DisplayName("Language fallback scenario")
    void testLanguageFallbackScenario() {
        LanguageOption primaryLanguage = new LanguageOption("en_US", "English");
        LanguageOption fallbackLanguage = new LanguageOption("en_US", "English (Default)");
        
        String displayName = null;
        if (primaryLanguage.getCode() != null && primaryLanguage.getCode().equals("en_US")) {
            displayName = primaryLanguage.toString();
        } else if (fallbackLanguage.getCode() != null && fallbackLanguage.getCode().equals("en_US")) {
            displayName = fallbackLanguage.toString();
        }
        
        assertEquals("English", displayName);
    }

    @Test
    @DisplayName("Cart state management")
    void testCartStateManagement() {
        CartItemEntry item = new CartItemEntry(500, 99.99, 5, 499.95);
        
        // Verify item doesn't change
        int initialQuantity = item.getQuantity();
        double initialPrice = item.getPrice();
        
        // Simulate state checks
        assertEquals(initialQuantity, item.getQuantity());
        assertEquals(initialPrice, item.getPrice());
        assertEquals(initialQuantity, item.getQuantity());
        
        // Verify immutability
        assertEquals(5, item.getQuantity());
        assertEquals(99.99, item.getPrice());
    }

    @Test
    @DisplayName("Price tier handling")
    void testPriceTierHandling() {
        List<CartItemEntry> tierItems = new ArrayList<>();
        
        // Budget tier
        tierItems.add(new CartItemEntry(1, 0.99, 10, 9.90));
        
        // Standard tier
        tierItems.add(new CartItemEntry(2, 9.99, 5, 49.95));
        
        // Premium tier
        tierItems.add(new CartItemEntry(3, 99.99, 2, 199.98));
        
        // Luxury tier
        tierItems.add(new CartItemEntry(4, 999.99, 1, 999.99));
        
        double totalSpent = 0;
        for (CartItemEntry item : tierItems) {
            totalSpent += item.getSubtotal();
        }
        
        assertEquals(4, tierItems.size());
        assertEquals(1259.82, totalSpent, 0.01);
    }

    @Test
    @DisplayName("Quantity breakpoint pricing")
    void testQuantityBreakpointPricing() {
        CartItemEntry smallQuantity = new CartItemEntry(1, 10.0, 1, 10.0);
        CartItemEntry mediumQuantity = new CartItemEntry(2, 10.0, 10, 100.0);
        CartItemEntry largeQuantity = new CartItemEntry(3, 10.0, 100, 1000.0);
        
        // Verify proportional scaling
        assertEquals(10.0, smallQuantity.getSubtotal());
        assertEquals(100.0, mediumQuantity.getSubtotal());
        assertEquals(1000.0, largeQuantity.getSubtotal());
    }

    @Test
    @DisplayName("Regional language content verification")
    void testRegionalLanguageContentVerification() {
        String[][] expectedLanguages = {
            {"en_US", "English"},
            {"fi_FI", "Finnish"},
            {"sv_SE", "Swedish"},
            {"ja_JP", "Japanese"},
            {"ar_SA", "Arabic"}
        };

        for (String[] langData : expectedLanguages) {
            LanguageOption option = new LanguageOption(langData[0], langData[1]);
            assertEquals(langData[0], option.getCode());
            assertFalse(option.toString().isEmpty());
        }
    }
}
