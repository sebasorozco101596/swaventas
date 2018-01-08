package swasolutions.com.wdpos.vo.clases_objeto;

/**
 * Created by sebas on 21/10/2017.
 */

public class Pedido {

    private int id;
    private String pedido;
    private String cliente;

    public Pedido(int id, String pedido, String cliente) {
        this.id = id;
        this.pedido = pedido;
        this.cliente = cliente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPedido() {
        return pedido;
    }

    public void setPedido(String pedido) {
        this.pedido = pedido;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

}
