package ni.edu.uam.Sistema_Nomina_Proyecto_Final_POO.modelo;

import lombok.*;
import org.openxava.annotations.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidad que representa un viaje de entrega de materiales.
 * Incluye montos específicos por rol para reflejar la compensación proporcional basada en participación.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@View(members = "fecha; destino; chofer; ayudante; montos [ montoChofer, montoAyudante ]") // Grupos en UI
public class Viaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Required
    private LocalDate fecha;

    @NotNull
    @Required
    private String destino;

    @ManyToOne
    @NotNull
    @ReferenceView("Simple") // Vista simplificada en dropdowns
    private Empleado chofer;

    @ManyToOne
    @ReferenceView("Simple")
    private Empleado ayudante; // Opcional

    @NotNull
    @Money // Formato moneda en UI OpenXava
    private BigDecimal montoChofer = BigDecimal.ZERO;

    @NotNull
    @Money
    private BigDecimal montoAyudante = BigDecimal.ZERO;
}
