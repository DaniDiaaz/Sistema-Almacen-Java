package ui;

import dao.VentaDAO;
import dao.ProductoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import modelo.Venta;
import modelo.Producto;

public class VentasView {

    private VentaDAO ventaDAO = new VentaDAO();

    private ObservableList<Venta> ventas = FXCollections.observableArrayList();

    private TableView<Venta> tabla = new TableView<>();

    public BorderPane getVista() {
        TableColumn<Venta, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setMinWidth(50);

        TableColumn<Venta, Integer> colIdProducto = new TableColumn<>("idProducto");
        colIdProducto.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        colIdProducto.setMinWidth(200);

        TableColumn<Venta, Integer> colCantidad = new TableColumn<>("Cantidad");
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colCantidad.setMinWidth(100);

        TableColumn<Venta, Double> colPrecioTotal = new TableColumn<>("Precio Total");
        colPrecioTotal.setCellValueFactory(new PropertyValueFactory<>("precioTotal"));
        colPrecioTotal.setMinWidth(80);

        TableColumn<Venta, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colFecha.setMinWidth(110);

        tabla.getColumns().addAll(colId, colIdProducto, colCantidad, colPrecioTotal, colFecha);
        tabla.setItems(ventas);
        cargarVentas();

        Button btnRegistrar = new Button("➕ Registrar Venta");
        Button btnActualizar = new Button("↺ Actualizar");

        btnRegistrar.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        btnActualizar.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");

        btnRegistrar.setOnAction(e -> mostrarDialogoRegistrar());
        btnActualizar.setOnAction(e -> cargarVentas());

        HBox botones = new HBox(10);
        botones.setPadding(new Insets(10, 0, 0, 0));
        botones.getChildren().addAll(btnRegistrar, btnActualizar);

        BorderPane panel = new BorderPane();
        panel.setPadding(new Insets(15));
        panel.setCenter(tabla);
        panel.setBottom(botones);

        return panel;

    }

    private void cargarVentas() {
        ventas.clear();
        ventas.addAll(new dao.VentaDAO().getTodasLasVentas());
    }

    private void mostrarDialogoRegistrar() {
        TextField txtIdProducto = new TextField();
        txtIdProducto.setPromptText("ID del producto");
        TextField txtCantidad = new TextField();
        txtCantidad.setPromptText("Cantidad");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        grid.addRow(0, new Label("ID Producto:"), txtIdProducto);
        grid.addRow(1, new Label("Cantidad:"), txtCantidad);

        Dialog<ButtonType> dialogo = new Dialog<>();
        dialogo.setTitle("Registrar Venta");
        dialogo.setHeaderText("Introduce los datos de la venta:");
        dialogo.getDialogPane().setContent(grid);
        dialogo.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialogo.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                try {
                    int idProducto = Integer.parseInt(txtIdProducto.getText().trim());
                    int cantidad = Integer.parseInt(txtCantidad.getText().trim());

                    // Buscamos el producto para ver su stock y precio
                    ProductoDAO productoDAO = new ProductoDAO();
                    Producto producto = productoDAO.getProductoPorId(idProducto);

                    if (producto == null) {
                        mostrarAlerta(Alert.AlertType.ERROR, "Error", "No existe ningún producto con ese ID.");
                        return;
                    }
                    // Comprobamos que hay suficiente stock
                    if (producto.getStock() < cantidad) {
                        mostrarAlerta(Alert.AlertType.ERROR, "Stock insuficiente",
                                "Stock disponible: " + producto.getStock());
                        return;
                    }
                    // Calculamos el precio total
                    double precioTotal = producto.getPrecio() * cantidad;

                    // Registramos la venta
                    Venta venta = new Venta();
                    venta.setIdProducto(idProducto);
                    venta.setCantidad(cantidad);
                    venta.setPrecioTotal(precioTotal);

                    if (ventaDAO.registrarVenta(venta)) {
                        // Actualizamos el stock del producto
                        int nuevoStock = producto.getStock() - cantidad;
                        productoDAO.actualizarStock(idProducto, nuevoStock);

                        cargarVentas();
                        mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito",
                                "Venta registrada. Total: " + precioTotal + "€");
                    }

                } catch (NumberFormatException ex) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error", "ID y cantidad deben ser números enteros.");
                }
            }
        });
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
