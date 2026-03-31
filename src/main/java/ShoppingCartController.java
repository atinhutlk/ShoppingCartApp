import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class ShoppingCartController {

    @FXML
    private ComboBox<LanguageOption> languageComboBox;

    @FXML
    private Label itemsLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label qtyLabel;

    @FXML
    private Label itemTotalLabel;

    @FXML
    private Label totalLabel;

    @FXML
    private TextField itemsField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField qtyField;

    @FXML
    private Label itemTotalResultLabel;

    @FXML
    private Label totalResultLabel;

    private double totalCart = 0;

    @FXML
    public void initialize() {
        Locale currentLocale = Locale.getDefault();
        ResourceBundle bundle = ResourceBundle.getBundle("MessagesBundle", currentLocale);

        languageComboBox.setItems(FXCollections.observableArrayList(
                new LanguageOption("en_US", bundle.getString("lang.english")),
                new LanguageOption("fi_FI", bundle.getString("lang.finnish")),
                new LanguageOption("sv_SE", bundle.getString("lang.swedish")),
                new LanguageOption("ja_JP", bundle.getString("lang.japanese")),
                new LanguageOption("ar_SA", bundle.getString("lang.arabic"))
        ));

        String currentCode = currentLocale.getLanguage() + "_" + currentLocale.getCountry();

        for (LanguageOption option : languageComboBox.getItems()) {
            if (option.getCode().equals(currentCode)) {
                languageComboBox.setValue(option);
                return;
            }
        }

        languageComboBox.setValue(languageComboBox.getItems().get(0));
    }

    @FXML
    private void handleCalculate() {
        try {
            double price = Double.parseDouble(priceField.getText());
            int qty = Integer.parseInt(qtyField.getText());

            double itemTotal = CartCalculator.itemTotal(price, qty);
            totalCart = CartCalculator.addToCart(totalCart, price, qty);

            itemTotalResultLabel.setText(String.valueOf(itemTotal));
            totalResultLabel.setText(String.valueOf(totalCart));
        } catch (NumberFormatException e) {
            itemTotalResultLabel.setText("Invalid input");
            totalResultLabel.setText("Invalid input");
        }
    }

    @FXML
    private void handleChangeLanguage() {
        try {
            LanguageOption selectedLanguage = languageComboBox.getValue();
            Locale locale;

            switch (selectedLanguage.getCode()) {
                case "fi_FI":
                    locale = new Locale("fi", "FI");
                    break;
                case "sv_SE":
                    locale = new Locale("sv", "SE");
                    break;
                case "ja_JP":
                    locale = new Locale("ja", "JP");
                    break;
                case "ar_SA":
                    locale = new Locale("ar", "SA");
                    break;
                default:
                    locale = new Locale("en", "US");
                    break;
            }

            Locale.setDefault(locale);

            ResourceBundle bundle = ResourceBundle.getBundle("MessagesBundle", locale);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShoppingCart.fxml"), bundle);
            Parent root = loader.load();

            Stage stage = (Stage) languageComboBox.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Nhut - Shopping Cart App");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}