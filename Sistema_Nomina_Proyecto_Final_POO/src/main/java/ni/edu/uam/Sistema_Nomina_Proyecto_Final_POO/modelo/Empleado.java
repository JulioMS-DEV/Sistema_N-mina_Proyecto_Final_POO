package ni.edu.uam.Sistema_Nomina_Proyecto_Final_POO.modelo;

import lombok.*;
import org.openxava.annotations.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Entidad que representa a un empleado de la ferretería, con rol (chofer o ayudante).
 * Su salario se calcula basado en los viajes en los que participa.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@View(members = "nombre; rol; viajes [ viajesComoChofer, viajesComoAyudante ]") // Vista default
@View(name = "Simple", members = "nombre; rol") // Vista simplificada para referencias
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Required // Para UI en OpenXava
    private String nombre;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Rol rol;

    @OneToMany(mappedBy = "chofer")
    @ListProperties("fecha, destino, montoChofer, montoAyudante") // Columnas en la lista de viajes
    private List<Viaje> viajesComoChofer;

    @OneToMany(mappedBy = "ayudante")
    @ListProperties("fecha, destino, montoChofer, montoAyudante")
    private List<Viaje> viajesComoAyudante;

    public enum Rol {
        CHOFER, AYUDANTE
    }
}