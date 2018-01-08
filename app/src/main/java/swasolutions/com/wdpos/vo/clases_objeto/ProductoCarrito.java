package swasolutions.com.wdpos.vo.clases_objeto;

/**
 * Created by sebas on 24/06/2017.
 */

public class ProductoCarrito {

    private int id;
    private String nombre;
    private int precio;
    private int cantidad;
    private String codigoProducto;

    public ProductoCarrito(){

    }

    public ProductoCarrito(int id, String nombre, int precio, int cantidad,String codigoProducto) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.codigoProducto=codigoProducto;
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

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }


}
