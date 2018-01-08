package swasolutions.com.wdpos.vo.clases_objeto;

/**
 * Created by sebas on 16/12/2017.
 */

public class Categoria {

    private int id;
    private String codigo;
    private String nombre;

    public Categoria(int id, String codigo, String nombre) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
