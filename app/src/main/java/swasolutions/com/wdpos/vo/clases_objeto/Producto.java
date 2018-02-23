package swasolutions.com.wdpos.vo.clases_objeto;

/**
 * Created by sebas on 23/06/2017.
 */

public class Producto {

    private int id;
    private String codigoProducto;
    private String nombre;
    private int unit;
    private String cost;
    private String precio1;
    private int precio2;
    private int precio3;
    private int precio4;
    private int precio5;
    private int precio6;
    private int categoryId;
    private String cantidad;
    private String type;
    private int estado; //si es 1 esta sin actualizar, si es 2 esta actualizado


    public Producto(int id, String nombre, int unit,String cost , String precio1, int precio2,
                    int precio3, int precio4, int precio5, int precio6, int categoryId,String type,
                    String codigoProducto, String cantidad,int estado) {
        this.id = id;
        this.nombre = nombre;
        this.unit=unit;
        this.cost=cost;
        this.precio1 = precio1;
        this.precio2= precio2;
        this.precio3=precio3;
        this.precio4=precio4;
        this.precio5=precio5;
        this.precio6=precio6;
        this.categoryId=categoryId;
        this.type=type;
        this.codigoProducto=codigoProducto;
        this.cantidad=cantidad;
        this.estado=estado;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
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
        return precio1;
    }

    public void setPrecio(String precio) {
        this.precio1 = precio;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getPrecio1() {
        return precio1;
    }

    public void setPrecio1(String precio1) {
        this.precio1 = precio1;
    }

    public int getPrecio2() {
        return precio2;
    }

    public void setPrecio2(int precio2) {
        this.precio2 = precio2;
    }

    public int getPrecio3() {
        return precio3;
    }

    public void setPrecio3(int precio3) {
        this.precio3 = precio3;
    }

    public int getPrecio4() {
        return precio4;
    }

    public void setPrecio4(int precio4) {
        this.precio4 = precio4;
    }

    public int getPrecio5() {
        return precio5;
    }

    public void setPrecio5(int precio5) {
        this.precio5 = precio5;
    }

    public int getPrecio6() {
        return precio6;
    }

    public void setPrecio6(int precio6) {
        this.precio6 = precio6;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
