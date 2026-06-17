package ui;

import dao.ProductoDAO;
import dao.VentaDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import modelo.Producto;
import modelo.Venta;

public class DashboardView {

    private ProductoDAO productoDAO = new ProductoDAO();
    private VentaDAO ventaDAO = new VentaDAO();

    public VBox getVista() {

        // ---- TÍTULO ----
        Label titulo = new Label("📊 Dashboard — Resumen del Almacén");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        // ---- CALCULAMOS LOS DATOS ----
        int totalProductos = productoDAO.getTodosLosProductos().size();

        int totalVentasHoy = ventaDAO.getVentasDeHoy().size();

        double recaudadoHoy = 0;
        for (Venta v : ventaDAO.getVentasDeHoy()) {
            recaudadoHoy += v.getPrecioTotal();
        }

        int stockBajoCount = productoDAO.getProductosStockBajo().size();

        // ---- TARJETAS ----
        HBox tarjetas = new HBox(15);
        tarjetas.setAlignment(Pos.CENTER);
        tarjetas.getChildren().addAll(
                crearTarjeta("📦 Productos",       String.valueOf(totalProductos),  "#3498db"),
                crearTarjeta("🛒 Ventas hoy",      String.valueOf(totalVentasHoy),  "#27ae60"),
                crearTarjeta("💰 Recaudado hoy",   recaudadoHoy + "€",             "#f39c12"),
                crearTarjeta("⚠️ Stock bajo",      String.valueOf(stockBajoCount),  "#e74c3c")
        );

        // ---- TABLA STOCK BAJO ----
        Label lblStockBajo = new Label("⚠️ Productos con stock bajo:");
        lblStockBajo.setFont(Font.font("Arial", FontWeight.BOLD, 15));

        TableView<Producto> tablaStock = crearTablaStockBajo();

        // ---- LAYOUT FINAL ----
        VBox panel = new VBox(20);
        panel.setPadding(new Insets(25));
        panel.getChildren().addAll(titulo, new Separator(), tarjetas, new Separator(), lblStockBajo, tablaStock);

        return panel;
    }

    /**
     * Crea una tarjeta visual con título, número y color de fondo
     */
    private VBox crearTarjeta(String titulo, String valor, String color) {
        VBox tarjeta = new VBox(8);
        tarjeta.setAlignment(Pos.CENTER);
        tarjeta.setPadding(new Insets(20));
        tarjeta.setPrefWidth(170);
        tarjeta.setStyle(
                "-fx-background-color: " + color + ";" +
                        "-fx-background-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"
        );

        Label lblTitulo = new Label(titulo);
        lblTitulo.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        lblTitulo.setTextFill(Color.WHITE);

        Label lblValor = new Label(valor);
        lblValor.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        lblValor.setTextFill(Color.WHITE);

        tarjeta.getChildren().addAll(lblTitulo, lblValor);
        return tarjeta;
    }

    /**
     * Crea la tabla de productos con stock bajo
     */
    private TableView<Producto> crearTablaStockBajo() {
        TableView<Producto> tabla = new TableView<>();
        tabla.setMaxHeight(200);
        tabla.setPlaceholder(new Label("✅ Todos los productos tienen stock suficiente."));

        TableColumn<Producto, String> colNombre = new TableColumn<>("Producto");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombre.setPrefWidth(250);

        TableColumn<Producto, Integer> colStock = new TableColumn<>("Stock Actual");
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colStock.setPrefWidth(120);

        TableColumn<Producto, Integer> colStockMin = new TableColumn<>("Stock Mínimo");
        colStockMin.setCellValueFactory(new PropertyValueFactory<>("stock_minimo"));
        colStockMin.setPrefWidth(120);

        tabla.getColumns().addAll(colNombre, colStock, colStockMin);
        tabla.getItems().addAll(productoDAO.getProductosStockBajo());

        return tabla;
    }
}