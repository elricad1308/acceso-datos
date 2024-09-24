package mx.uacam.fi.its.pa;

import java.sql.SQLException;
import java.util.regex.PatternSyntaxException;

import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class VistaConsultaController {

    @FXML private BorderPane borderPane;
    @FXML private TextField filterTextField;
    @FXML private TextArea queryTextArea;

    private final String DEFAULT_QUERY = "SELECT * FROM personas";

    private Modelo tableModel;
    private TableRowSorter<TableModel> sorter;

    public void initialize () {
        queryTextArea.setText("SELECT * FROM personas");

        try {
            // Crea el modelo con la consulta por defecto
            tableModel = new Modelo();
            tableModel.setQuery(DEFAULT_QUERY);

            // Crea la JTable basada en el modelo
            JTable resultTable = new JTable(tableModel);

            // Configura el ordenamiento de la JTable
            sorter = new TableRowSorter<TableModel>(tableModel);
            resultTable.setRowSorter(sorter);

            // Configura un SwingNode para mostrar la JTable
            SwingNode swingNode = new SwingNode();
            swingNode.setContent(new JScrollPane(resultTable));
            borderPane.setCenter(swingNode);
        } catch (SQLException ex) {
            displayAlert(AlertType.ERROR, "Error de conexión", ex.getMessage());
            tableModel.disconnect();
            System.exit(1);
        }
    }

    @FXML
    void applyFilterButtonPressed(ActionEvent event) {
        String text = filterTextField.getText();

        if (text.trim().length() == 0)
            sorter.setRowFilter(null);
        else {
            try {
                sorter.setRowFilter(RowFilter.regexFilter(text));
            } catch (PatternSyntaxException ex) {
                displayAlert(
                        AlertType.ERROR,
                        "Error de filtro",
                        "Expresión regular inválida"
                );
            }
        }

    }

    @FXML
    void submitQueryButtonPressed(ActionEvent event) {
        // Realiza una nueva consulta
        try {
            tableModel.setQuery(queryTextArea.getText());
        } catch (SQLException ex) {
            displayAlert(
                    AlertType.ERROR,
                    "Error de base de datos",
                    ex.getMessage()
            );

            // Intenta recuperarse de una consulta inválida
            // ejecutando el query por defecto
            try {
                tableModel.setQuery(DEFAULT_QUERY);
                queryTextArea.setText(DEFAULT_QUERY);
            } catch (SQLException ex1) {
                displayAlert(
                        AlertType.ERROR,
                        "Error de base de datos",
                        ex1.getMessage()
                );

                tableModel.disconnect();
                System.exit(1);
            }
        }
    }

    private void displayAlert (AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
