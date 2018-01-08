package swasolutions.com.wdpos.vo.clases_objeto;

/**
 * Created by sebas on 25/06/2017.
 */

public class Cliente {


    private String cedula;
    private String name;
    private String compania;
    private String direccion;
    private String telefono;
    private String id;
    private int group_id;



    public Cliente() {
    }

    public Cliente(String cedula, String name, String compania, String direccion, String telefono,String id,
    int group_id) {
        this.cedula = cedula;
        this.name = name;
        this.compania = compania;
        this.direccion = direccion;
        this.telefono = telefono;
        this.id=id;
        this.group_id=group_id;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompania() {
        return compania;
    }


    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public int getGroup_id() {
        return group_id;
    }

}
