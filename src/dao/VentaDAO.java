package dao;

import modelo.Venta;

import java.sql.*;
import java.util.ArrayList;

public class VentaDAO {

    public boolean registrarVenta(Venta v) {
        String sql = "INSERT INTO ventas (id_producto, cantidad, precio_total) VALUES (?,?,?)";

        try (
                Connection connection = ConexionDB.getConexion();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, v.getIdProducto());
            ps.setInt(2, v.getCantidad());
            ps.setDouble(3, v.getPrecioTotal());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar venta: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Venta> getTodasLasVentas() {
        ArrayList<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM ventas";

        try (
                Connection connection = ConexionDB.getConexion();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql);
        ) {
            while (rs.next()) {
                Venta v = new Venta();

                v.setId(rs.getInt("id"));
                v.setIdProducto(rs.getInt("id_producto"));
                v.setCantidad(rs.getInt("cantidad"));
                v.setPrecioTotal(rs.getDouble("precio_total"));
                v.setFecha(rs.getString("fecha"));

                ventas.add(v);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ventas: " + e.getMessage());
        }
        return ventas;
    }

    public ArrayList<Venta> getVentasDeHoy() {
        ArrayList<Venta> ventasDeHoy = new ArrayList<>();
        String sql = "SELECT * FROM ventas WHERE DATE(fecha) = CURDATE()";

        try (
                Connection connection = ConexionDB.getConexion();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql);
        ) {
            while (rs.next()) {
                Venta v = new Venta();

                v.setId(rs.getInt("id"));
                v.setIdProducto(rs.getInt("id_producto"));
                v.setCantidad(rs.getInt("cantidad"));
                v.setPrecioTotal(rs.getDouble("precio_total"));
                v.setFecha(rs.getString("fecha"));

                ventasDeHoy.add(v);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener las ventas de hoy: " + e.getMessage());
        }
        return ventasDeHoy;
    }
}
