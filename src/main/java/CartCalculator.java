public class CartCalculator {

    public static double itemTotal(double price, int qty) {
        return price * qty;
    }

    public static double addToCart(double currentTotal, double price, int qty) {
        return currentTotal + itemTotal(price, qty);
    }

}