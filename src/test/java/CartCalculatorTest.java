import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CartCalculatorTest {

    @Test
    void testItemTotal() {
        assertEquals(
                20,
                CartCalculator.itemTotal(10, 2)
        );
    }

    @Test
    void testAddToCart() {

        double total = 0;

        total = CartCalculator.addToCart(total, 10, 2);
        total = CartCalculator.addToCart(total, 5, 3);

        assertEquals(35, total);
    }
}