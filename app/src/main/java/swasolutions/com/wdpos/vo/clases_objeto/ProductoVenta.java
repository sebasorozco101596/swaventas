package swasolutions.com.wdpos.vo.clases_objeto;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sebas on 2/07/2017.
 */

public class ProductoVenta {

    private int id;
    private int idVenta;
    private int idProducto;
    private String codigoProducto;
    private String nombre;
    private int precioUnitario;
    private int cantidad;
    private int idVendedor;

    public ProductoVenta() {
    }

    public ProductoVenta(int id, int idVenta, int idProducto, String codigoProducto,String nombre,
                         int precioUnitario, int cantidad,int idVendedor) {
        this.id = id;
        this.idVenta = idVenta;
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
        this.codigoProducto=codigoProducto;
        this.idVendedor=idVendedor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(int precioUnitario) {
        this.precioUnitario = precioUnitario;
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

    public int getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(int idVendedor) {
        this.idVendedor = idVendedor;
    }


    public String toJSON(){

        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("idVenta", "1");
            jsonObject.put("idProducto",""+getIdProducto());
            jsonObject.put("codigoProducto",""+getCodigoProducto());
            jsonObject.put("nombreProducto", getNombre());
            jsonObject.put("precio", ""+getPrecioUnitario());
            jsonObject.put("cantidad", ""+getCantidad());
            jsonObject.put("subtotal", ""+(getCantidad()*getPrecioUnitario()));
            return jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
}
