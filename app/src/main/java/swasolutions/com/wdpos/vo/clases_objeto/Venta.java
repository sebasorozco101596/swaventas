package swasolutions.com.wdpos.vo.clases_objeto;

import java.util.ArrayList;

/**
 * Created by sebas on 2/07/2017.
 */

public class Venta {

    private int id;
    private String fecha;
    private String referencia;
    private int idVendedor;
    private String cliente;
    private String cedulaCliente;
    private int total;
    private String estadoVenta;
    private int pagadoPorCliente;
    private int cantidadProductos;
    private String tipo;
    private String idCliente;
    private ArrayList<ProductoVenta> productos;
    private int existe;
    private String nota;
    private int credito;

    public Venta() {
    }

    public Venta(int id, String fecha, String referencia, int idVendedor, String cliente,
                 String cedulaCliente, int total, String estadoVenta, int pagadoPorCliente,
                 int cantidadProductos, ArrayList<ProductoVenta> productos) {
        this.id = id;
        this.fecha = fecha;
        this.referencia = referencia;
        this.idVendedor = idVendedor;
        this.cliente = cliente;
        this.cedulaCliente = cedulaCliente;
        this.total = total;
        this.estadoVenta = estadoVenta;
        this.pagadoPorCliente = pagadoPorCliente;
        this.cantidadProductos= cantidadProductos;
        this.productos=productos;
    }


    public Venta(int id, String fecha, String referencia, int idVendedor, String cliente,
                 String cedulaCliente, int total, String estadoVenta, int pagadoPorCliente,
                 int cantidadProductos) {
        this.id = id;
        this.fecha = fecha;
        this.referencia = referencia;
        this.idVendedor = idVendedor;
        this.cliente = cliente;
        this.cedulaCliente = cedulaCliente;
        this.total = total;
        this.estadoVenta = estadoVenta;
        this.pagadoPorCliente = pagadoPorCliente;
        this.cantidadProductos = cantidadProductos;

    }

    public Venta(int id, String fecha, String referencia, int idVendedor, String cliente,
                 String cedulaCliente, int total, String estadoVenta, int pagadoCliente,
                 int cantidadProductos, String idCliente,int existe,String nota,int credito) {
        this.id = id;
        this.fecha = fecha;
        this.referencia = referencia;
        this.idVendedor = idVendedor;
        this.cliente = cliente;
        this.cedulaCliente = cedulaCliente;
        this.total = total;
        this.estadoVenta = estadoVenta;
        this.pagadoPorCliente=pagadoCliente;
        this.cantidadProductos = cantidadProductos;
        this.idCliente=idCliente;
        this.existe=existe;
        this.nota=nota;
        this.credito=credito;


    }

    public Venta(int id, String fecha, String referencia, int idVendedor, String cliente,
                 String cedulaCliente, int total, String estadoVenta, int pagadoCliente,
                 int cantidadProductos, String idCliente,int existe,String nota) {
        this.id = id;
        this.fecha = fecha;
        this.referencia = referencia;
        this.idVendedor = idVendedor;
        this.cliente = cliente;
        this.cedulaCliente = cedulaCliente;
        this.total = total;
        this.estadoVenta = estadoVenta;
        this.pagadoPorCliente=pagadoCliente;
        this.cantidadProductos = cantidadProductos;
        this.idCliente=idCliente;
        this.existe=existe;
        this.nota=nota;


    }

    public Venta(int id, String fecha, String referencia, int idVendedor, String cliente,
                 String cedulaCliente, int total, String estadoVenta, int pagadoCliente,
                 int cantidadProductos, String idCliente,int existe) {
        this.id = id;
        this.fecha = fecha;
        this.referencia = referencia;
        this.idVendedor = idVendedor;
        this.cliente = cliente;
        this.cedulaCliente = cedulaCliente;
        this.total = total;
        this.estadoVenta = estadoVenta;
        this.pagadoPorCliente=pagadoCliente;
        this.cantidadProductos = cantidadProductos;
        this.idCliente=idCliente;
        this.existe=existe;

    }

    public int getCredito() {
        return credito;
    }

    public void setCredito(int credito) {
        this.credito = credito;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public int getExiste() {
        return existe;
    }

    public void setExiste(int existe) {
        this.existe = existe;
    }

    public String getIdCliente() {
        return idCliente;
    }
    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getCedulaCliente() {
        return cedulaCliente;
    }

    public void setCedulaCliente(String cedulaCliente) {
        this.cedulaCliente = cedulaCliente;
    }

    public ArrayList<ProductoVenta> getProductos() {
        return productos;
    }

    public void setProductos(ArrayList<ProductoVenta> productos) {
        this.productos = productos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public int getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(int idVendedor) {
        this.idVendedor = idVendedor;
    }

    public String getVendedor() {
        return cliente;
    }

    public void setVendedor(String cliente) {
        this.cliente = cliente;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getEstadoVenta() {
        return estadoVenta;
    }

    public void setEstadoVenta(String estadoVenta) {
        this.estadoVenta = estadoVenta;
    }

    public int getPagadoPorCliente() {
        return pagadoPorCliente;
    }

    public void setPagadoPorCliente(int pagadoPorCliente) {
        this.pagadoPorCliente = pagadoPorCliente;
    }

    public int getCantidadProductos() {
        return cantidadProductos;
    }

    public void setCantidadProductos(int cantidadProductos) {
        this.cantidadProductos = cantidadProductos;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
