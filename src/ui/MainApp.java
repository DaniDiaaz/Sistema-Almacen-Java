package ui;

import dao.ConexionDB;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab tabDashboard = new Tab("📊  Dashboard");
        Tab tabProductos = new Tab("📦  Productos");
        Tab tabVentas = new Tab("🛒  Ventas");

        tabPane.getTabs().addAll(tabDashboard, tabProductos, tabVentas);

        Scene scene = new Scene(tabPane, 950, 650);
        stage.setTitle("🏪 Sistema de Gestión de Almacén");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(550);

        ProductosView productosView = new ProductosView();
        tabProductos.setContent(productosView.getVista());

        VentasView ventasView = new VentasView();
        tabVentas.setContent(ventasView.getVista());

        DashboardView dashboardView = new DashboardView();
        tabDashboard.setContent(dashboardView.getVista());

        // Cerramos la conexión MySQL al cerrar la ventana
        stage.setOnCloseRequest(e -> ConexionDB.cerrarConexion());

        scene.getRoot().setStyle("-fx-font-family: 'Segoe UI Emoji';");

        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
