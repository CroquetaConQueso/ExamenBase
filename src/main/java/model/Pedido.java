package model;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class Pedido implements Serializable {
    @NotNull(message = "ID Pedido obligatorio")
    private int idPedido;
    
    @NotNull(message = "Flor obligatoria")
    private Flor flor;
    
    private int cantidad;

    public Pedido() {}
    public Pedido(int id, Flor f, int c) { this.idPedido=id; this.flor=f; this.cantidad=c; }

    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }
    public Flor getFlor() { return flor; }
    public void setFlor(Flor flor) { this.flor = flor; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}