package mx.uacam.fi.its.pa;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class EjemploJDBC {
    public static void main (String[] args) {
        final String HOST = "jdbc:mariadb://localhost:3306/db_libros";
        final String USER = "root";
        final String PASS = "2014C0Ord1n4$$";

        final String QUERY = "SELECT * FROM personas";

        // Usa try-with-resources para conectar y consultar la bd
        try (
                Connection conexion = DriverManager.getConnection(HOST, USER, PASS);
                Statement sentencia = conexion.createStatement();
                ResultSet resultado = sentencia.executeQuery(QUERY);
        ) {
            ResultSetMetaData metadata = resultado.getMetaData();
            int columnas = metadata.getColumnCount();

            System.out.printf("Tabla Autores de db_libros:%n%n");

            // Despliega los nombres de las columnas en el ResultSet
            for (int i = 1; i <= columnas; i++)
                System.out.printf("%-8s\t", metadata.getColumnName(i));
            System.out.println();

            // Despliega los resultados de la consulta
            while (resultado.next()) {
                for (int i = 1; i <= columnas; i++)
                    System.out.printf("%-8s\t", resultado.getObject(i));
                System.out.println();
            }
        } // Los métodos para cerrar los recursos se llaman automáticamente
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
