package ni.edu.uam.Sistema_Nomina_Proyecto_Final_POO.modelo;

import lombok.*;
import org.openxava.annotations.*;
import org.openxava.jpa.XPersistence;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

/**
 * Entidad que representa el pago semanal de un empleado.
 * Calcula sumando montos de chofer y montos individuales como ayudante.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@View(members = "empleado; fechaPago; montoTotal; fechaGeneracion")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    @ReferenceView("Simple")
    private Empleado empleado;

    @NotNull
    @Required
    private LocalDate fechaPago;

    @NotNull
    private LocalDate fechaGeneracion = LocalDate.now();

    @ReadOnly
    @Money
    @Depends("fechaPago, empleado")
    public BigDecimal getMontoTotal() {
        BigDecimal total = BigDecimal.ZERO;
        if (fechaPago == null || empleado == null) return total;

        try {
            WeekFields weekFields = WeekFields.of(Locale.getDefault());
            LocalDate inicioSemana = fechaPago.with(weekFields.dayOfWeek(), 1);
            LocalDate finSemana = inicioSemana.plusDays(6);

            EntityManager em = XPersistence.getManager();

            // Suma como chofer
            String jpqlChofer = "SELECT SUM(v.montoChofer) FROM Viaje v WHERE v.chofer = :emp AND v.fecha BETWEEN :start AND :end";
            Query queryChofer = em.createQuery(jpqlChofer);
            queryChofer.setParameter("emp", empleado);
            queryChofer.setParameter("start", inicioSemana);
            queryChofer.setParameter("end", finSemana);
            Number sumChofer = (Number) queryChofer.getSingleResult();
            if (sumChofer != null) total = total.add(BigDecimal.valueOf(sumChofer.doubleValue()));

            // Suma como ayudante (montos individuales)
            String jpqlAyudante = "SELECT SUM(pa.monto) FROM ParticipacionAyudante pa WHERE pa.empleado = :emp AND pa.viaje.fecha BETWEEN :start AND :end";
            Query queryAyudante = em.createQuery(jpqlAyudante);
            queryAyudante.setParameter("emp", empleado);
            queryAyudante.setParameter("start", inicioSemana);
            queryAyudante.setParameter("end", finSemana);
            Number sumAyudante = (Number) queryAyudante.getSingleResult();
            if (sumAyudante != null) total = total.add(BigDecimal.valueOf(sumAyudante.doubleValue()));
        } catch (IllegalArgumentException e) {
            return total;
        }

        return total;
    }
}