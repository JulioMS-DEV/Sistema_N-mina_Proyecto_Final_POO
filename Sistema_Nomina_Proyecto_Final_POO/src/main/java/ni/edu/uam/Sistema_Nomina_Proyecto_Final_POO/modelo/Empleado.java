package ni.edu.uam.Sistema_Nomina_Proyecto_Final_POO.modelo;

import lombok.*;
import org.openxava.annotations.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa a un empleado de la ferretería.
 * Su salario se calcula basado en los viajes en los que participa.
 * Atributos adicionales: fecha de contratación, número de identificación, teléfono y dirección.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@View(members = "datosBasicos [ nombre; numeroIdentificacion; fechaContratacion ]; " +
        "contacto [ telefono; direccion ]; " +
        "viajes [ viajesComoChofer, viajesComoAyudante ]")
@View(name = "Simple", members = "nombre; numeroIdentificacion")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Required
    private String nombre;

    @NotNull
    @Required
    @Column(unique = true)
    private String numeroIdentificacion;

    @NotNull
    @Required
    private LocalDate fechaContratacion;

    private String telefono;

    private String direccion;

    @OneToMany(mappedBy = "chofer")
    @ListProperties("fecha, destino, montoChofer")
    private List<Viaje> viajesComoChofer = new ArrayList<>();

    @OneToMany(mappedBy = "empleado")
    @ListProperties("viaje.fecha, viaje.destino, monto") // Muestra monto individual
    private List<ParticipacionAyudante> viajesComoAyudante = new ArrayList<>();
}