package swasolutions.com.wdpos.vo.clases_objeto;

/**
 * Created by sebas on 2/07/2017.
 */

public class Abono {


    private int id;
    private String estadoVenta;
    private int pagado;
    private int pagoPayment;
    private int idVendedor;
    private String fecha;
    private String referencia;
    private String cedCliente;
    private int credito;


    public Abono(int id, String estadoVenta, int pagado, int idVendedor, String fecha,
                 String referencia,int pagoPayment,String cedCliente) {
        this.id = id;
        this.estadoVenta = estadoVenta;
        this.pagado = pagado;
        this.idVendedor = idVendedor;
        this.fecha = fecha;
        this.referencia=referencia;
        this.pagoPayment=pagoPayment;
        this.cedCliente= cedCliente;
    }

    public Abono(int id, String estadoVenta, int pagado, int idVendedor, String fecha,
                 String referencia,int pagoPayment,String cedCliente,int credito) {
        this.id = id;
        this.estadoVenta = estadoVenta;
        this.pagado = pagado;
        this.idVendedor = idVendedor;
        this.fecha = fecha;
        this.referencia=referencia;
        this.pagoPayment=pagoPayment;
        this.cedCliente= cedCliente;
        this.credito=credito;
    }

    public int getCredito() {
        return credito;
    }

    public void setCredito(int credito) {
        this.credito = credito;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEstadoVenta() {
        return estadoVenta;
    }

    public int getPagado() {
        return pagado;
    }

    public void setPagado(int pagado) {
        this.pagado = pagado;
    }

    public int getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(int idVendedor) {
        this.idVendedor = idVendedor;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getPagoPayment() {
        return pagoPayment;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getCedCliente() {
        return cedCliente;
    }

}
