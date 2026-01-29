package model;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

@Entity
@Table(name = "FLORES")
public class Flor implements Serializable {
    
    @Id
    @Column(name = "ID_FLOR")
    @NotNull(message = "El ID es obligatorio")
    private int idFlor;

    @Column(name = "NOMBRE_FLOR")
    @NotBlank(message = "El nombre es obligatorio")
    private String nombreFlor;

    @Column(name = "COLOR")
    @NotBlank(message = "El color es obligatorio")
    private String color;

    public Flor() {}
    public Flor(int id, String n, String c) { this.idFlor=id; this.nombreFlor=n; this.color=c; }

    // Getters y Setters
    public int getIdFlor() { return idFlor; }
    public void setIdFlor(int idFlor) { this.idFlor = idFlor; }
    public String getNombreFlor() { return nombreFlor; }
    public void setNombreFlor(String nombreFlor) { this.nombreFlor = nombreFlor; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    @Override public String toString() { return nombreFlor + " (" + color + ")"; }
}