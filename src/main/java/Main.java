import java.util.*;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Select a language :");
        System.out.println("1. English");
        System.out.println("2. Finnish");
        System.out.println("3. Swedish");
        int lang = sc.nextInt();

        Locale locale;

        switch (lang) {
            case 1:
                locale = new Locale("en", "US");
                break;
            case 2:
                locale = new Locale("fi", "FI");
                break;
            case 3:
                locale = new Locale("sv", "SE");
                break;
            default:
                locale = new Locale("en", "US");
                break;
        }

        ResourceBundle msg =
                ResourceBundle.getBundle("MessagesBundle", locale);

        System.out.println(msg.getString("prompt.items"));

        int n = sc.nextInt();

        double totalCart = 0;

        for (int i = 0; i < n; i++) {

            int itemNumber = i + 1;

            System.out.println(
                    msg.getString("prompt.price") + itemNumber + ":"
            );
            double price = sc.nextDouble();

            System.out.println(
                    msg.getString("prompt.qty") + itemNumber + ":"
            );
            int qty = sc.nextInt();

            totalCart =
                    CartCalculator.addToCart(
                            totalCart,
                            price,
                            qty
                    );
        }

        System.out.println(
                msg.getString("total") + " " + totalCart
        );
    }
}