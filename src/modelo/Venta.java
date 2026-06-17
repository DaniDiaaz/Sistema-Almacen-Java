package modelo;

public class Venta {

    private int id;
    private int idProducto;
    private int cantidad;
    private double precioTotal;
    private String fecha;

    public Venta() {

    }

    public Venta(int id, int idProducto, int cantidad, double precioTotal, String fecha) {
        this.id = id;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioTotal = precioTotal;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Venta [id=" + id + ", idProducto=" + idProducto + ", cantidad=" + cantidad + ", precioTotal="
                + precioTotal + ", fecha=" + fecha + "]";
    }


}
