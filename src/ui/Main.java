package ui;

import dao.ConexionDB;
import dao.ProductoDAO;
import dao.VentaDAO;
import modelo.Producto;
import modelo.Venta;

import java.sql.SQLOutput;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        // Intentamos conectarnos
        // ConexionDB.getConexion();

        // Cerramos conexión
        //ConexionDB.cerrarConexion();

        ProductoDAO ProductoDAO = new ProductoDAO();
        VentaDAO VentaDAO = new VentaDAO();

        // -- Test 1: Obtener todos los productos:
        System.out.println("=== TODOS LOS PRODUCTOS ===");
        ArrayList<Producto> productos = ProductoDAO.getTodosLosProductos();
        for (Producto p : productos) {
            System.out.println(p);
        }

        // -- Test 2: Insertar un producto nuevo:
        System.out.println("\n=== INSERTANDO PRODUCTO ===");
        Producto nuevo = new Producto();
        nuevo.setNombre("Monitor 24\"");
        nuevo.setPrecio(199.99);
        nuevo.setStock(8);
        nuevo.setStock_minimo(3);
        nuevo.setIdCategoria(1);
        nuevo.setIdProveedor(1);
        boolean insertado = ProductoDAO.insertarProducto(nuevo);
        System.out.println("Insertado: " + insertado);

        // -- Test 3: Productos con stock bajo:
        System.out.println("\n=== STOCK BAJO ===");
        ArrayList<Producto> stockBajo = ProductoDAO.getProductosStockBajo();
        for (Producto p : stockBajo) {
            System.out.println(p.getNombre() + " - Stock: " + p.getStock() + " (mínimo: " + p.getStock_minimo() + ")");
        }

        // -- Test 4: Registrar una venta:
        System.out.println("\n=== REGISTRANDO UNA VENTA ===");
        Venta venta = new Venta();
        venta.setIdProducto(1);
        venta.setCantidad(2);
        venta.setPrecioTotal(51.98);
        boolean vendido = VentaDAO.registrarVenta(venta);
        System.out.println("Venta registrada: " + vendido);

        // -- Test 5: Ventas de hoy:
        System.out.println("\n=== VENTAS DE HOY ===");
        ArrayList<Venta> ventasHoy = VentaDAO.getVentasDeHoy();
        for (Venta v : ventasHoy) {
            System.out.println("Producto ID: " + v.getIdProducto() + " - Cantidad: " + v.getCantidad() + " - Precio: " + v.getPrecioTotal());
        }

        ConexionDB.cerrarConexion();

    }
}
