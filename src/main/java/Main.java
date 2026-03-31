import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Locale locale = new Locale("en", "US");
        ResourceBundle bundle = ResourceBundle.getBundle("MessagesBundle", locale);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShoppingCart.fxml"), bundle);
        Scene scene = new Scene(loader.load());

        stage.setTitle("Nhut - Shopping Cart App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}