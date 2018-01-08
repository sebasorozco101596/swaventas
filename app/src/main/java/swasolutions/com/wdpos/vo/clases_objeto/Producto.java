package swasolutions.com.wdpos.vo.clases_objeto;

/**
 * Created by sebas on 23/06/2017.
 */

public class Producto {

    private int id;
    private String codigoProducto;
    private String nombre;
    private String precio;
    private String cantidad;

    public Producto() {
    }

    public Producto(int id, String nombre, String precio,String codigoProducto,String cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.codigoProducto=codigoProducto;
        this.cantidad=cantidad;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
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

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
}
