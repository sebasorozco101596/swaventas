package swasolutions.com.wdpos.vo.clases_objeto;

/**
 * Created by sebas on 18/08/2017.
 */

public class ClienteCompleto {

    private String id;
    private String grupo;
    private String nombre;
    private String cedula;
    private String direccion;
    private String ciudad;
    private String estado;
    private String pais;
    private String telefono;
    private int tipo;

    public ClienteCompleto(String id, String grupo, String nombre, String cedula, String direccion,
                           String ciudad, String estado, String pais, String telefono,int tipo) {
        this.id = id;
        this.grupo = grupo;
        this.nombre = nombre;
        this.cedula = cedula;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.estado = estado;
        this.pais = pais;
        this.telefono = telefono;
        this.tipo=tipo;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

}
