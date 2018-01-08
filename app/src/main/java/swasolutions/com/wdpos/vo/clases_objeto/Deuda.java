package swasolutions.com.wdpos.vo.clases_objeto;

/**
 * Created by sebas on 26/06/2017.
 */

public class Deuda {

    private int id;
    private String idCliente;
    private String fecha;
    private String referencia;
    private String comprador;
    private String total;
    private String pagado;

    public Deuda() {
    }

    public Deuda(int id, String idCliente, String fecha, String referencia, String comprador, String total, String pagado) {
        this.id = id;
        this.idCliente = idCliente;
        this.fecha = fecha;
        this.referencia = referencia;
        this.comprador = comprador;
        this.total = total;
        this.pagado = pagado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPagado() {
        return pagado;
    }

    public void setPagado(String pagado) {
        this.pagado = pagado;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
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

    public String getComprador() {
        return comprador;
    }

    public void setComprador(String comprador) {
        this.comprador = comprador;
    }
}
