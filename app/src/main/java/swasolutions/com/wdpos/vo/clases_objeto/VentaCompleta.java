package swasolutions.com.wdpos.vo.clases_objeto;

import java.util.ArrayList;

/**
 * Created by sebas on 19/08/2017.
 */

public class VentaCompleta {


    private Venta venta;
    private ArrayList<ProductoVenta> productosVenta;

    public VentaCompleta() {
    }

    public VentaCompleta(Venta venta,ArrayList<ProductoVenta> productos) {

        productosVenta= new ArrayList<>();
        this.venta=venta;
        this.productosVenta=productos;

    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public ArrayList<ProductoVenta> getProductos() {
        return productosVenta;
    }

    public void setProductos(ArrayList<ProductoVenta> productos) {
        this.productosVenta = productos;
    }
}
