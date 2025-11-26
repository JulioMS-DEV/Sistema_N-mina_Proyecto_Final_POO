package ni.edu.uam.Sistema_Nomina_Proyecto_Final_POO.modelo;

import lombok.*;
import org.openxava.annotations.*;
import org.openxava.util.XavaException; // Para lanzar errores en UI

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entidad que representa un viaje de entrega de materiales.
 * Soporta un chofer (uno) y múltiples ayudantes con montos individuales.
 * Validación: No permite empleados duplicados en la lista de ayudantes (incluyendo al chofer si actúa como ayudante).
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@View(members = "fecha; destino; chofer; montos [ montoChofer ]; ayudantes") // Muestra colección de ayudantes
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
    @ReferenceView("Simple")
    private Empleado chofer;

    @OneToMany(mappedBy = "viaje", cascade = CascadeType.ALL, orphanRemoval = true)
    @ListProperties("empleado.nombre, monto") // Columnas en la lista UI de ayudantes
    private List<ParticipacionAyudante> ayudantes = new ArrayList<>();

    @NotNull
    @Money
    private BigDecimal montoChofer = BigDecimal.ZERO;

    // Validación antes de insertar o actualizar
    @PrePersist
    @PreUpdate
    private void validarAyudantesUnicos() {
        Set<Long> empleadoIds = new HashSet<>();
        for (ParticipacionAyudante pa : ayudantes) {
            Long empId = pa.getEmpleado().getId();
            if (empleadoIds.contains(empId)) {
                throw new XavaException("No se puede agregar el mismo empleado más de una vez como ayudante en este viaje.");
            }
            empleadoIds.add(empId);
        }
    }
}