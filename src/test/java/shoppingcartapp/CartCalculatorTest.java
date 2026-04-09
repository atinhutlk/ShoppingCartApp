package shoppingcartapp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("CartCalculator Tests")
class CartCalculatorTest {

    @Test
    @DisplayName("Constructor exists and has correct class name")
    void testConstructorExists() {
        assertEquals("CartCalculator", CartCalculator.class.getSimpleName());
    }

    @Test
    @DisplayName("Item total calculation with whole numbers")
    void testItemTotal() {
        assertEquals(20.0, CartCalculator.itemTotal(10, 2));
    }

    @Test
    @DisplayName("Item total calculation with decimal values")
    void testItemTotalWithDecimalValues() {
        assertEquals(7.5, CartCalculator.itemTotal(2.5, 3));
    }

    @Test
    @DisplayName("Add items to empty cart")
    void testAddToCart() {
        double total = 0;

        total = CartCalculator.addToCart(total, 10, 2);
        total = CartCalculator.addToCart(total, 5, 3);

        assertEquals(35.0, total);
    }

    @Test
    @DisplayName("Add items to existing cart total")
    void testAddToCartWithExistingTotal() {
        assertEquals(42.5, CartCalculator.addToCart(30.0, 2.5, 5));
    }

    @Test
    @DisplayName("Item total with zero quantity returns zero")
    void testItemTotalWithZeroQuantity() {
        assertEquals(0.0, CartCalculator.itemTotal(10, 0));
    }

    @Test
    @DisplayName("Item total with price zero")
    void testItemTotalWithZeroPrice() {
        assertEquals(0.0, CartCalculator.itemTotal(0.0, 5));
    }

    @Test
    @DisplayName("Item total with large quantity")
    void testItemTotalWithLargeQuantity() {
        assertEquals(1000.0, CartCalculator.itemTotal(1.0, 1000));
    }

    @Test
    @DisplayName("Item total with large price")
    void testItemTotalWithLargePrice() {
        assertEquals(50000.0, CartCalculator.itemTotal(10000.0, 5));
    }

    @Test
    @DisplayName("Item total with fractional price and quantity")
    void testItemTotalWithFractionalValues() {
        assertEquals(10.5, CartCalculator.itemTotal(3.5, 3));
    }

    @Test
    @DisplayName("Add to cart with multiple items and decimal precision")
    void testAddToCartWithMultipleDecimalItems() {
        double total = 0.0;
        total = CartCalculator.addToCart(total, 9.99, 1);
        total = CartCalculator.addToCart(total, 5.5, 2);
        assertEquals(20.99, total, 0.001);
    }

    @Test
    @DisplayName("Add to cart maintains accumulation correctly")
    void testAddToCartAccumulation() {
        double total = 10.0;
        total = CartCalculator.addToCart(total, 5.0, 1);
        total = CartCalculator.addToCart(total, 3.0, 2);
        assertEquals(21.0, total);
    }

    @Test
    @DisplayName("Item total with single quantity")
    void testItemTotalWithSingleQuantity() {
        assertEquals(15.75, CartCalculator.itemTotal(15.75, 1));
    }

    @Test
    @DisplayName("Add to cart starting from zero with multiple items")
    void testAddToCartFromZero() {
        double total = CartCalculator.addToCart(0, 100.0, 1);
        total = CartCalculator.addToCart(total, 50.0, 2);
        total = CartCalculator.addToCart(total, 25.0, 4);
        assertEquals(300.0, total);
    }

    @Test
    @DisplayName("Item total precision with many decimal places")
    void testItemTotalDecimalPrecision() {
        assertEquals(33.30, CartCalculator.itemTotal(11.1, 3), 0.01);
    }

    @Test
    @DisplayName("Item total with very small decimal values")
    void testItemTotalWithVerySmallDecimals() {
        assertEquals(0.03, CartCalculator.itemTotal(0.01, 3), 0.001);
    }

    @Test
    @DisplayName("Add to cart with negative price")
    void testAddToCartWithNegativePrice() {
        double total = 100.0;
        total = CartCalculator.addToCart(total, -10.0, 2);
        assertEquals(80.0, total);
    }

    @Test
    @DisplayName("Add to cart with negative quantity")
    void testAddToCartWithNegativeQuantity() {
        double total = 100.0;
        total = CartCalculator.addToCart(total, 10.0, -2);
        assertEquals(80.0, total);
    }

    @Test
    @DisplayName("Item total with boundary integer values")
    void testItemTotalWithBoundaryIntegers() {
        assertEquals(Integer.MAX_VALUE * 1.0, CartCalculator.itemTotal(1.0, Integer.MAX_VALUE), 1.0);
    }

    @Test
    @DisplayName("Multiple consecutive additions maintain precision")
    void testMultipleConsecutiveAdditions() {
        double total = 0;
        for (int i = 0; i < 100; i++) {
            total = CartCalculator.addToCart(total, 1.01, 1);
        }
        assertEquals(101.0, total, 0.1);
    }

    @Test
    @DisplayName("Add to cart with exact decimal arithmetic")
    void testAddToCartExactDecimalArithmetic() {
        double total = 0;
        total = CartCalculator.addToCart(total, 0.1, 1);
        total = CartCalculator.addToCart(total, 0.2, 1);
        assertEquals(0.3, total, 0.001);
    }

    @Test
    @DisplayName("Item total with price equal to quantity")
    void testItemTotalWithPriceEqualToQuantity() {
        assertEquals(100.0, CartCalculator.itemTotal(10.0, 10));
    }

    @Test
    @DisplayName("Add to cart accumulates correctly with mixed quantities")
    void testAddToCartWithMixedQuantities() {
        double total = 0;
        total = CartCalculator.addToCart(total, 5.0, 1);
        total = CartCalculator.addToCart(total, 10.0, 2);
        total = CartCalculator.addToCart(total, 15.0, 3);
        total = CartCalculator.addToCart(total, 20.0, 4);
        assertEquals(150.0, total);
    }

    @Test
    @DisplayName("Item total consistency across multiple calls")
    void testItemTotalConsistency() {
        double result1 = CartCalculator.itemTotal(12.5, 8);
        double result2 = CartCalculator.itemTotal(12.5, 8);
        assertEquals(result1, result2);
        assertEquals(100.0, result1);
    }

    @Test
    @DisplayName("Private constructor is covered and utility class remains non-instantiable by default")
    void testPrivateConstructorViaReflection() throws Exception {
        Constructor<CartCalculator> constructor = CartCalculator.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));

        constructor.setAccessible(true);
        CartCalculator instance = constructor.newInstance();
        assertNotNull(instance);
        assertEquals(CartCalculator.class, instance.getClass());
    }
}