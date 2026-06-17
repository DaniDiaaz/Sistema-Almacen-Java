package ui;

import dao.ProductoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import modelo.Producto;

public class ProductosView {
    // El DAO para hablar con MySQL
    private ProductoDAO productoDAO = new ProductoDAO();

    // ObservableList: cuando cambia, la tabla se actualiza automáticamente
    private ObservableList<Producto> listaProductos = FXCollections.observableArrayList();

    // La tabla visual
    private TableView<Producto> tabla = new TableView<>();

    public BorderPane getVista() {

        // ---- TABLA ----
        // Cada columna se vincula a un getter del objeto Producto
        // "nombre" → llama a getNombre() automáticamente
        TableColumn<Producto, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(50);

        TableColumn<Producto, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombre.setPrefWidth(200);

        TableColumn<Producto, Double> colPrecio = new TableColumn<>("Precio");
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colPrecio.setPrefWidth(100);

        TableColumn<Producto, Integer> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colStock.setPrefWidth(80);

        TableColumn<Producto, Integer> colStockMin = new TableColumn<>("Stock Mínimo");
        colStockMin.setCellValueFactory(new PropertyValueFactory<>("stock_minimo"));
        colStockMin.setPrefWidth(110);

        // Añadimos las colunmas a la tabla
        tabla.getColumns().addAll(colId, colNombre, colPrecio, colStock, colStockMin);

        // Vinculamos la lista observable a la tabla
        tabla.setItems(listaProductos);

        // Cargamos los datos de MySQL
        cargarProductos();

        // Layout
        BorderPane panel = new BorderPane();
        panel.setPadding(new Insets(15));
        panel.setCenter(tabla);

        // --- BOTONES ---
        Button btnAnadir = new Button("➕ Añadir");
        Button btnEliminar = new Button("🗑 Eliminar");
        Button btnActualizar = new Button("↺ Actualizar");
        Button btnEditar = new Button("✏️ Editar");

        // Estilos
        btnAnadir.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        btnEliminar.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        btnActualizar.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        btnEditar.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold;");

        // Acciones
        btnAnadir.setOnAction(e -> mostrarDialogoAnadir());
        btnEliminar.setOnAction(e -> eliminarSeleccionado());
        btnActualizar.setOnAction(e -> cargarProductos());
        btnEditar.setOnAction(e -> mostrarDialogoEditar());

        // HBox: coloca los botones en fila horizontal
        HBox botones = new HBox(10); // 10px de espacio entre botones
        botones.setPadding(new Insets(10, 0, 0, 0));
        botones.getChildren().addAll(btnAnadir, btnEliminar, btnActualizar, btnEditar);

        panel.setBottom(botones);

        return panel;
    }

    private void cargarProductos() {
        // Limpiamos la lista y la rellenamos desde MySQL
        listaProductos.clear();
        listaProductos.addAll(productoDAO.getTodosLosProductos());
    }

    private void mostrarDialogoAnadir() {
        // Campos del formulario
        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre del producto");
        TextField txtPrecio = new TextField();
        txtPrecio.setPromptText("Precio (ej: 19.99)");
        TextField txtStock = new TextField();
        txtStock.setPromptText("Stock inicial");
        TextField txtStockMin = new TextField();
        txtStockMin.setPromptText("Stock mínimo");

        // Grid para organizar los campos
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        grid.addRow(0, new Label("Nombre:"), txtNombre);
        grid.addRow(1, new Label("Precio:"), txtPrecio);
        grid.addRow(2, new Label("Stock:"), txtStock);
        grid.addRow(3, new Label("Stock mínimo:"), txtStockMin);

        // Ventana emergente
        Dialog<ButtonType> dialogo = new Dialog<>();
        dialogo.setTitle("Añadir Producto");
        dialogo.setHeaderText("Introduce los datos del nuevo producto:");
        dialogo.getDialogPane().setContent(grid);
        dialogo.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Procesamos si pulsa OK
        dialogo.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                try {
                    Producto p = new Producto();
                    p.setNombre(txtNombre.getText().trim());
                    p.setPrecio(Double.parseDouble(txtPrecio.getText().trim()));
                    p.setStock(Integer.parseInt(txtStock.getText().trim()));
                    p.setStock_minimo(Integer.parseInt(txtStockMin.getText().trim()));
                    p.setIdCategoria(1); // Por ahora categoría por defecto
                    p.setIdProveedor(1); // Por ahora proveedor por defecto

                    if (productoDAO.insertarProducto(p)) {
                        cargarProductos(); // Recargamos la tabla
                        mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Producto añadido correctamente.");
                    }
                } catch (NumberFormatException ex) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error", "Precio y stock deben ser números.");
                }
            }
        });
    }

    private void eliminarSeleccionado() {
        Producto seleccionado = tabla.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona un producto de la tabla primero.");
            return;
        }

        // Confirmación antes de eliminar
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar");
        confirmacion.setContentText("¿Eliminar el producto: " + seleccionado.getNombre() + "?");
        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                if (productoDAO.eliminarProducto(seleccionado.getId())) {
                    cargarProductos();
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Eliminado", "Producto eliminado correctamente.");
                }
            }
        });
    }

    private void mostrarDialogoEditar() {
        // Comprobamos que hay un producto seleccionado
        Producto seleccionado = tabla.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Selecciona un producto de la tabla primero.");
            return;
        }

        // Campos ya rellenos con los datos actuales
        TextField txtNombre = new TextField(seleccionado.getNombre());
        TextField txtPrecio = new TextField(String.valueOf(seleccionado.getPrecio()));
        TextField txtStock = new TextField(String.valueOf(seleccionado.getStock()));
        TextField txtStockMin = new TextField(String.valueOf(seleccionado.getStock_minimo()));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        grid.addRow(0, new Label("Nombre:"), txtNombre);
        grid.addRow(1, new Label("Precio:"), txtPrecio);
        grid.addRow(2, new Label("Stock:"), txtStock);
        grid.addRow(3, new Label("Stock mínimo:"), txtStockMin);

        Dialog<ButtonType> dialogo = new Dialog<>();
        dialogo.setTitle("Editar Producto");
        dialogo.setHeaderText("Editando: " + seleccionado.getNombre());
        dialogo.getDialogPane().setContent(grid);
        dialogo.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialogo.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                try {
                    // Actualizamos el objeto con los nuevos valores
                    seleccionado.setNombre(txtNombre.getText().trim());
                    seleccionado.setPrecio(Double.parseDouble(txtPrecio.getText().trim()));
                    seleccionado.setStock(Integer.parseInt(txtStock.getText().trim()));
                    seleccionado.setStock_minimo(Integer.parseInt(txtStockMin.getText().trim()));

                    if (productoDAO.editarProducto(seleccionado)) {
                        cargarProductos();
                        mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Producto actualizado correctamente.");
                    }
                } catch (NumberFormatException ex) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error", "Precio y stock deben ser números.");
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
