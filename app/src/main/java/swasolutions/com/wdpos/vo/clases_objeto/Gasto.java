package swasolutions.com.wdpos.vo.clases_objeto;

/**
 * Created by sebas on 2/07/2017.
 */

public class Gasto {

    private int id;
    private String fecha;
    private String referencia;
    private int dineroGastado;
    private String vendedor;
    private String descripcion;

    public Gasto() {
    }

    public Gasto(int id, String fecha, String referencia, int dineroGastado, String vendedor,String descripcion) {
        this.id = id;
        this.fecha = fecha;
        this.referencia = referencia;
        this.dineroGastado = dineroGastado;
        this.vendedor = vendedor;
        this.descripcion=descripcion;
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

    public int getDineroGastado() {
        return dineroGastado;
    }

    public void setDineroGastado(int dineroGastado) {
        this.dineroGastado = dineroGastado;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
