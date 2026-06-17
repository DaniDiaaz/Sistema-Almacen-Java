package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * CLASE ConexionDB
 *
 * Se encarga de abrir y cerrar la conexión con MySQL. Usamos el patrón
 * Singleton: solo existe UNA conexión en todo el programa.
 *
 * JDBC funciona así: - URL: le dice a Java dónde está MySQL y qué base de datos
 * usar - DriverManager: es el que abre la conexión físicamente - Connection: el
 * objeto que representa la conexión activa
 */

public class ConexionDB {

    // Datos de conexión
    private static final String URL = "jdbc:mysql://localhost:3306/almacen";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "root";

    // La única instancia de la conexión (patrón Singleton)
    private static Connection conexion = null;

    /**
     * Devuelve la conexión activa. Si no existe todavía, la crea. Si ya existe,
     * devuelve la misma.
     */

    public static Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
                System.out.println("✅ Conexión con MySQL establecida.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar con MySQL: " + e.getMessage());
        }
        return conexion;
    }

    /**
     * Cierra la conexión cuando ya no se necesita. Se llamará al cerrar la
     * aplicación.
     */

    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("🔌 Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }

}