package modelo;

public class Producto {

    private int id;
    private String nombre;
    private double precio;
    private int stock;
    private int stock_minimo;
    private int idCategoria;
    private int idProveedor;

    public Producto() {

    }

    public Producto(int id, String nombre, double precio, int stock, int stock_minimo, int idCategoria,
                    int idProveedor) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.stock_minimo = stock_minimo;
        this.idCategoria = idCategoria;
        this.idProveedor = idProveedor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getStock_minimo() {
        return stock_minimo;
    }

    public void setStock_minimo(int stock_minimo) {
        this.stock_minimo = stock_minimo;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    @Override
    public String toString() {
        return "Producto [id=" + id + ", nombre=" + nombre + ", precio=" + precio + ", stock=" + stock
                + ", stock_minimo=" + stock_minimo + ", idCategoria=" + idCategoria + ", idProveedor=" + idProveedor
                + "]";
    }

}
