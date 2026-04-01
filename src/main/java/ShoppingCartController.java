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

import java.util.Locale;
import java.util.ResourceBundle;

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

    private double totalCart = 0;
    private Locale currentLocale = new Locale("en", "US");
    private ResourceBundle bundle;
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

        setLanguage(currentLocale);
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
        if (updatingLanguageComboBox) {
            return;
        }

        LanguageOption selected = languageComboBox.getValue();
        if (selected == null) {
            return;
        }

        switch (selected.getCode()) {
            case "fi_FI":
                setLanguage(new Locale("fi", "FI"));
                break;
            case "sv_SE":
                setLanguage(new Locale("sv", "SE"));
                break;
            case "ja_JP":
                setLanguage(new Locale("ja", "JP"));
                break;
            case "ar_SA":
                setLanguage(new Locale("ar", "SA"));
                break;
            default:
                setLanguage(new Locale("en", "US"));
                break;
        }
    }

    private void setLanguage(Locale locale) {
        currentLocale = locale;
        Locale.setDefault(locale);
        bundle = ResourceBundle.getBundle("MessagesBundle", locale);

        updateTexts();
        updateLanguageComboBox();
        applyTextDirection(locale);
    }

    private void updateTexts() {
        titleLabel.setText(bundle.getString("title"));
        itemsLabel.setText(bundle.getString("prompt.items"));
        priceLabel.setText(bundle.getString("prompt.price"));
        qtyLabel.setText(bundle.getString("prompt.qty"));
        calculateButton.setText(bundle.getString("button.calculate"));
        itemTotalLabel.setText(bundle.getString("item.total"));
        totalLabel.setText(bundle.getString("total"));
    }

    private void updateLanguageComboBox() {
        updatingLanguageComboBox = true;

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