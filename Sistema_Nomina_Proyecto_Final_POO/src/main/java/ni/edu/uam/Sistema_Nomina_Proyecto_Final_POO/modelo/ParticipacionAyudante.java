package ni.edu.uam.Sistema_Nomina_Proyecto_Final_POO.modelo;

import lombok.*;
import org.openxava.annotations.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Entidad intermedia para la participación de un ayudante en un viaje, con monto individual.
 * Permite múltiples ayudantes por viaje, cada uno con pago basado en su contribución.
 * Nota: Oculta en UI principal; se maneja desde la colección en Viaje.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@View(members = "empleado; monto") // UI simple para editar en colecciones
public class ParticipacionAyudante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    @ReferenceView("Simple")
    private Empleado empleado;

    @ManyToOne
    @NotNull
    private Viaje viaje;

    @NotNull
    @Money
    private BigDecimal monto = BigDecimal.ZERO; // Monto individual para este ayudante
}