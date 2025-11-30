package ni.edu.uam.Sistema_Nomina_Proyecto_Final_POO.modelo;

import lombok.*;
import org.openxava.annotations.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import ni.edu.uam.Sistema_Nomina_Proyecto_Final_POO.validacion.CedulaNica;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa a un empleado de la ferretería.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@View(members = "datosBasicos [ nombres; apellidos; cedula; fechaContratacion ]; " +
        "contacto [ telefono; direccion ]; " +
        "viajes [ viajesComoChofer, viajesComoAyudante ]")
@View(name = "Simple", members = "nombres, apellidos")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Required
    private String nombres;

    @NotNull
    @Required
    private String apellidos;

    @NotNull
    @Required
    @Column(unique = true)
    @CedulaNica
    private String cedula;

    @NotNull
    @Required
    private LocalDate fechaContratacion;

    @Pattern(regexp = "\\d{8}", message = "El número de teléfono debe tener 8 dígitos")
    private String telefono;

    private String direccion;

    @OneToMany(mappedBy = "chofer")
    @ListProperties("fecha, destino, montoChofer")
    private List<Viaje> viajesComoChofer = new ArrayList<>();

    @OneToMany(mappedBy = "empleado")
    @ListProperties("viaje.fecha, viaje.destino, monto")
    private List<ParticipacionAyudante> viajesComoAyudante = new ArrayList<>();
}
