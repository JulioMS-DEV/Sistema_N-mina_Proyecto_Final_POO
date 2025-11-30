package ni.edu.uam.Sistema_Nomina_Proyecto_Final_POO.modelo;

import lombok.Getter;
import lombok.Setter;
import org.openxava.jpa.XPersistence;

import javax.persistence.*;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "participacion_ayudante",
        uniqueConstraints = @UniqueConstraint(columnNames = {"viaje_id", "empleado_id"}))
public class ParticipacionAyudante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private Viaje viaje;

    @ManyToOne
    @NotNull
    private Empleado empleado;

    private BigDecimal monto;

    // getters / setters ...

    @PrePersist
    @PreUpdate
    private void validarNoDuplicado() {
        if (viaje == null || empleado == null) return;

        EntityManager em = XPersistence.getManager();

        Long existente = (Long) em.createQuery(
                        "SELECT COUNT(pa) FROM ParticipacionAyudante pa WHERE pa.viaje = :v AND pa.empleado = :e AND (pa.id IS NULL OR pa.id <> :id)")
                .setParameter("v", viaje)
                .setParameter("e", empleado)
                .setParameter("id", id == null ? -1L : id)
                .getSingleResult();

        if (existente != null && existente > 0) {
            throw new ValidationException("El ayudante ya está agregado en este viaje.");
        }
    }
}
