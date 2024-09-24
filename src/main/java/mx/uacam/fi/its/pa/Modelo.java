package mx.uacam.fi.its.pa;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import javax.swing.table.AbstractTableModel;

public class Modelo extends AbstractTableModel {

    private final Connection conexion;
    private final Statement sentencia;
    private ResultSet resultado;
    private ResultSetMetaData metadata;
    private int numeroFilas;

    private boolean conectado = false;

    private final String HOST = "jdbc:mariadb://localhost:3306/db_escuela_full";
    private final String USER = "root";
    private final String PASS = "2014C0Ord1n4$$";

    public Modelo() throws SQLException {
        // Conecta con la base de datos
        conexion = DriverManager.getConnection(HOST, USER, PASS);

        // Crea un Statement para consultar la base de datos
        sentencia = conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        // Actualiza el estado de conexión
        conectado = true;
    }

    @Override
    public Class getColumnClass (int columna) {
        if (!conectado)
            throw new IllegalStateException("Sin conexión");

        try {
            String nombre = metadata.getColumnClassName(columna + 1);

            return Class.forName(nombre);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return Object.class;
    }

    @Override
    public int getColumnCount () {
        if (!conectado)
            throw new IllegalStateException("Sin conexión");

        try {
            return metadata.getColumnCount();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return 0;
    }

    @Override
    public String getColumnName (int columna) {
        if (!conectado)
            throw new IllegalStateException("Sin conexión");

        try {
            return metadata.getColumnName(columna + 1);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return "";
    }

    @Override
    public int getRowCount () {
        if (!conectado)
            throw new IllegalStateException("Sin conexión");

        return numeroFilas;
    }

    @Override
    public Object getValueAt (int fila, int columna) {
        if (!conectado)
            throw new IllegalStateException("Sin conexión");

        try {
            resultado.absolute(fila + 1);
            return resultado.getObject(columna + 1);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return "";
    }

    public void setQuery (String query) throws SQLException {
        if (!conectado)
            throw new IllegalStateException("Sin conexión");

        // Ejecuta la consulta proporcionada
        resultado = sentencia.executeQuery(query);

        // Obtiene los metadatos del ResultSet
        metadata = resultado.getMetaData();

        // Determina el número de filas en el ResultSet
        resultado.last();
        numeroFilas = resultado.getRow();

        // Notifica a la tabla que el modelo ha cambiado
        super.fireTableStructureChanged();
    }

    public void disconnect () {
        if (!conectado) return;

        try {
            resultado.close();
            sentencia.close();
            conexion.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            conectado = false;
        }
    }
}
