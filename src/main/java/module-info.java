module AccesoDatos {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires org.mariadb.jdbc;

    opens mx.uacam.fi.its.pa to javafx.fxml, javafx.graphics;
}
