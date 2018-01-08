package swasolutions.com.wdpos.vo.clases_objeto;

/**
 * Created by sebas on 21/12/2017.
 */

public class Devolucion {

    private int id;
    private String cedula;
    private int valor;
    private String codigoProducto;
    private String nombreProducto;

    public Devolucion(int id, String cedula, int valor, String codigoProducto,String nombreProducto) {
        this.id = id;
        this.cedula = cedula;
        this.valor = valor;
        this.codigoProducto = codigoProducto;
        this.nombreProducto=nombreProducto;
    }


    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }
}
