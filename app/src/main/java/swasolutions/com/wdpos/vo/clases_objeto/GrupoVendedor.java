package swasolutions.com.wdpos.vo.clases_objeto;

/**
 * Created by sebas on 3/11/2017.
 */

public class GrupoVendedor {

    private int id;
    private String name;
    private int percent;

    public GrupoVendedor(int id, String name, int percent) {
        this.id = id;
        this.name = name;
        this.percent = percent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }
}
