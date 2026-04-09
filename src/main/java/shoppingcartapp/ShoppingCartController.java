package shoppingcartapp;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ShoppingCartController {

    @FXML
    private VBox rootVBox;

    @FXML
    private ComboBox<LanguageOption> languageComboBox;

    @FXML
    private Label titleLabel;

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
    private Button calculateButton;

    @FXML
    private Label itemTotalResultLabel;

    @FXML
    private Label totalResultLabel;

    private double totalCart = 0.0;
    private int totalItems = 0;
    private String currentLanguageCode = "en_US";
    private Locale currentLocale = Locale.US;
    private Map<String, String> localizedTexts = new HashMap<>();
    private final List<CartItemEntry> cartItems = new ArrayList<>();
    private final LocalizationService localizationService = new LocalizationService();
    private final CartService cartService = new CartService();
    private boolean updatingLanguageComboBox = false;

    @FXML
    public void initialize() {
        languageComboBox.setConverter(new StringConverter<LanguageOption>() {
            @Override
            public String toString(LanguageOption option) {
                return option == null ? "" : option.toString();
            }

            @Override
            public LanguageOption fromString(String string) {
                return null;
            }
        });

        setLanguage(currentLanguageCode);
    }

    @FXML
    private void handleCalculate() {
        try {
            int itemNumber = Integer.parseInt(itemsField.getText());
            double price = Double.parseDouble(priceField.getText());
            int qty = Integer.parseInt(qtyField.getText());

            double itemTotal = CartCalculator.itemTotal(price, qty);
            totalCart = CartCalculator.addToCart(totalCart, price, qty);
            totalItems += qty;

            cartItems.add(new CartItemEntry(itemNumber, price, qty, itemTotal));
            saveCartData();

            itemTotalResultLabel.setText(String.format(Locale.US, "%.2f", itemTotal));
            totalResultLabel.setText(String.format(Locale.US, "%.2f", totalCart));
        } catch (NumberFormatException e) {
            String invalidInput = getText("error.invalidInput", "Invalid input");
            itemTotalResultLabel.setText(invalidInput);
            totalResultLabel.setText(invalidInput);
        }
    }

    private void saveCartData() {
        try {
            cartService.saveCartRecord(totalItems, totalCart, currentLanguageCode, cartItems);
        } catch (SQLException e) {
            // Keep UI responsive while logging DB failures for troubleshooting.
            System.err.println("Failed to save cart data: " + e.getMessage());
        }
    }

    @FXML
    private void handleChangeLanguage() {
        if (updatingLanguageComboBox) {
            return;
        }

        LanguageOption selected = languageComboBox.getValue();
        if (selected == null) {
            return;
        }

        setLanguage(selected.getCode());
    }

    private void setLanguage(String languageCode) {
        currentLanguageCode = languageCode;
        currentLocale = toLocale(languageCode);
        Locale.setDefault(currentLocale);

        loadLocalizedTexts();

        updateTexts();
        updateLanguageComboBox();
        applyTextDirection(currentLocale);
    }

    private Locale toLocale(String languageCode) {
        String[] parts = languageCode.split("_");
        if (parts.length == 2) {
            return new Locale(parts[0], parts[1]);
        }
        return Locale.US;
    }

    private void loadLocalizedTexts() {
        try {
            localizedTexts = localizationService.getLocalizedStrings(currentLanguageCode);
            if (localizedTexts.isEmpty() && !"en_US".equals(currentLanguageCode)) {
                localizedTexts = localizationService.getLocalizedStrings("en_US");
            }
        } catch (SQLException e) {
            localizedTexts = new HashMap<>();
            System.err.println("Failed to load localization from database: " + e.getMessage());
        }
    }

    private void updateTexts() {
        titleLabel.setText(getText("title", "Shopping Cart"));
        itemsLabel.setText(getText("prompt.items", "Item Number"));
        priceLabel.setText(getText("prompt.price", "Price"));
        qtyLabel.setText(getText("prompt.qty", "Quantity"));
        calculateButton.setText(getText("button.calculate", "Calculate"));
        itemTotalLabel.setText(getText("item.total", "Item Total"));
        totalLabel.setText(getText("total", "Cart Total"));
    }

    private String getText(String key, String fallback) {
        return localizedTexts.getOrDefault(key, fallback);
    }

    private void updateLanguageComboBox() {
        updatingLanguageComboBox = true;

        languageComboBox.setItems(FXCollections.observableArrayList(
                new LanguageOption("en_US", getText("lang.english", "English")),
                new LanguageOption("fi_FI", getText("lang.finnish", "Finnish")),
                new LanguageOption("sv_SE", getText("lang.swedish", "Swedish")),
                new LanguageOption("ja_JP", getText("lang.japanese", "Japanese")),
                new LanguageOption("ar_SA", getText("lang.arabic", "Arabic"))
        ));

        for (LanguageOption option : languageComboBox.getItems()) {
            if (option.getCode().equals(currentLanguageCode)) {
                languageComboBox.setValue(option);
                break;
            }
        }

        updatingLanguageComboBox = false;
    }

    private void applyTextDirection(Locale locale) {
        String lang = locale.getLanguage();
        boolean isRTL = lang.equals("ar") || lang.equals("fa") || lang.equals("ur") || lang.equals("he");

        Platform.runLater(() -> {
            if (rootVBox != null) {
                rootVBox.setNodeOrientation(
                        isRTL ? NodeOrientation.RIGHT_TO_LEFT
                                : NodeOrientation.LEFT_TO_RIGHT
                );
            }

            String alignment = isRTL
                    ? "-fx-text-alignment: right; -fx-alignment: center-right;"
                    : "-fx-text-alignment: left; -fx-alignment: center-left;";

            itemsField.setStyle(alignment);
            priceField.setStyle(alignment);
            qtyField.setStyle(alignment);
        });
    }
}