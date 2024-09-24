package mx.uacam.fi.its.pa;

import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.application.Application;

public class VistaConsulta extends Application {

    @Override
    public void start (Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/VistaConsulta.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Vista Consulta");
        stage.setScene(scene);
        stage.show();
    }

    public static void main (String[] args) {
        Application.launch(args);
    }
}
