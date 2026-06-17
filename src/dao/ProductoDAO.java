package dao;

import modelo.Producto;

import java.sql.*;
import java.util.ArrayList;


public class ProductoDAO {

    // SELECT * FROM productos, obtener todos los productos de la lista
    public ArrayList<Producto> getTodosLosProductos() {
        // Lista vacía donde iremos metiendo los productos
        ArrayList<Producto> productos = new ArrayList<>();

        String sql = "SELECT * FROM productos";
        try (
                Connection connection = ConexionDB.getConexion();
                Statement statement = connection.createStatement();
                // ResultSet es como una tabla de resultados
                // ExecuteQuery se usa para SELECT
                ResultSet rs = statement.executeQuery(sql);
        ) {
            // rs.next() avanza el cursor a la siguiente fila y devuelve true si existe
            while (rs.next()) {
                Producto p = new Producto();

                // Leemos cada columna de la fila actual por nombre de columna
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setPrecio(rs.getDouble("precio"));
                p.setStock(rs.getInt("stock"));
                p.setStock_minimo(rs.getInt("stock_minimo"));
                p.setIdCategoria(rs.getInt("id_categoria"));
                p.setIdProveedor(rs.getInt("id_proveedor"));

                // Añadimos el producto a la lista
                productos.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener productos: " + e.getMessage());
        }
        return productos;
    }

    // INSERT, insertar productos en la tabla
    public boolean insertarProducto(Producto p) {
        // Los ? son huecos que rellenaremos después, esto se llama PreparedStatement y
        // evita errores de sintaxis y ataques de inyección SQL
        String sql = "INSERT INTO productos (nombre, precio, stock, stock_minimo, id_categoria, id_proveedor) VALUES (?, ?, ?, ?, ?, ?)";
        try (

                // Abrimos la conexión con MySQL
                Connection connection = ConexionDB.getConexion();

                // Preparamos la sentencia SQL (con los ? pendientes de rellenar)
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            // Rellenamos los ? por posición
            ps.setString(1, p.getNombre());
            ps.setDouble(2, p.getPrecio());
            ps.setInt(3, p.getStock());
            ps.setInt(4, p.getStock_minimo());
            ps.setInt(5, p.getIdCategoria());
            ps.setInt(6, p.getIdProveedor());

            // Ejecutamos la sentencia, executeUpdate se usa para INSERT, UPDATE, DELETE y devuelve el número de filas afectadas
            return ps.executeUpdate() > 0; // true si se insertó al menos 1 fila

        } catch (SQLException e) {
            System.err.println("Error al insertar producto: " + e.getMessage());
            return false;
        }
    }

    // DELETE, eliminar productos por su id
    public boolean eliminarProducto(int id) {
        String sql = "DELETE FROM productos WHERE id = ?";
        try (
                Connection connection = ConexionDB.getConexion();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0; // true si se eliminó algo

        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Producto> getProductosStockBajo() {
        ArrayList<Producto> stockBajo = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE stock <= stock_minimo";

        try (
                Connection connection = ConexionDB.getConexion();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql);
        ) {
            while (rs.next()) {
                Producto p = new Producto();

                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setPrecio(rs.getDouble("precio"));
                p.setStock(rs.getInt("stock"));
                p.setStock_minimo(rs.getInt("stock_minimo"));
                p.setIdCategoria(rs.getInt("id_categoria"));
                p.setIdProveedor(rs.getInt("id_proveedor"));

                stockBajo.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener productos con stock bajo: " + e.getMessage());
        }
        return stockBajo;
    }

    // Actualiza el stock de un producto
    public boolean actualizarStock(int idProducto, int nuevoStock) {
        String sql = "UPDATE productos SET stock = ? WHERE id = ?";
        try (
                Connection connection = ConexionDB.getConexion();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, nuevoStock);
            ps.setInt(2, idProducto);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar stock: " + e.getMessage());
            return false;
        }
    }

    // Buscar producto por ID
    public Producto getProductoPorId(int id) {
        String sql = "SELECT * FROM productos WHERE id = ?";
        try (
                Connection connection = ConexionDB.getConexion();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setPrecio(rs.getDouble("precio"));
                p.setStock(rs.getInt("stock"));
                p.setStock_minimo(rs.getInt("stock_minimo"));
                p.setIdCategoria(rs.getInt("id_categoria"));
                p.setIdProveedor(rs.getInt("id_proveedor"));
                return p;
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener producto: " + e.getMessage());
        }
        return null;
    }

    public boolean editarProducto(Producto p) {
        String sql = "UPDATE productos SET nombre = ?, precio = ?, stock = ?, stock_minimo = ? WHERE id = ?";
        try (
                Connection connection = ConexionDB.getConexion();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, p.getNombre());
            ps.setDouble(2, p.getPrecio());
            ps.setInt(3, p.getStock());
            ps.setInt(4, p.getStock_minimo());
            ps.setInt(5, p.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al editar producto: " + e.getMessage());
            return false;
        }
    }
}
