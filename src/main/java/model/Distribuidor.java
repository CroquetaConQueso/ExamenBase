package model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Distribuidor implements Serializable {
    private int idDistribuidor;
    private String cif;
    private String nombre;
    
    // Relación N:M (Un distribuidor tiene un set de Flores)
    private Set<Flor> flores = new HashSet<>();

    public Distribuidor() {}
    public Distribuidor(int id, String cif, String nombre) {
        this.idDistribuidor = id;
        this.cif = cif;
        this.nombre = nombre;
    }

    // Getters y Setters
    public int getIdDistribuidor() { return idDistribuidor; }
    public void setIdDistribuidor(int id) { this.idDistribuidor = id; }
    public String getCif() { return cif; }
    public void setCif(String cif) { this.cif = cif; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public Set<Flor> getFlores() { return flores; }
    public void setFlores(Set<Flor> flores) { this.flores = flores; }
    
    @Override public String toString() { return nombre + " (" + cif + ")"; }
}