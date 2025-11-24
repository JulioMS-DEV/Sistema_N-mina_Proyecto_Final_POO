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
 * El monto total se calcula automáticamente sumando los montos de viajes en la semana especificada.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@View(members = "empleado; semana; montoTotal; fechaGeneracion")
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
    private String semana; // Formato "YYYY-WW" ej. "2025-45"

    @NotNull
    private LocalDate fechaGeneracion = LocalDate.now();

    @ReadOnly // Hace que sea solo lectura en la UI de OpenXava
    @Money
    @Depends("semana, empleado") // Recalcula si cambian
    public BigDecimal getMontoTotal() {
        BigDecimal total = BigDecimal.ZERO;
        if (semana == null || empleado == null) return total;

        try {
            // Parsear semana a fechas de inicio/fin
            String[] parts = semana.split("-");
            if (parts.length != 2) return total;
            int year = Integer.parseInt(parts[0]);
            int week = Integer.parseInt(parts[1]);
            LocalDate start = LocalDate.ofYearDay(year, 1)
                    .with(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear(), week)
                    .with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1); // Lunes
            LocalDate end = start.plusDays(6); // Domingo

            // Query JPA para sumar montos relevantes
            EntityManager em = XPersistence.getManager();
            String jpql = "SELECT SUM(CASE WHEN v.chofer = :emp THEN v.montoChofer "
                    + "WHEN v.ayudante = :emp THEN v.montoAyudante ELSE 0 END) "
                    + "FROM Viaje v WHERE v.fecha BETWEEN :start AND :end";
            Query query = em.createQuery(jpql);
            query.setParameter("emp", empleado);
            query.setParameter("start", start);
            query.setParameter("end", end);
            Number result = (Number) query.getSingleResult();
            if (result != null) total = BigDecimal.valueOf(result.doubleValue());
        } catch (IllegalArgumentException e) {
            // Manejo de formato inválido de semana o errores en fechas (incluye NumberFormatException)
            return total;
        }

        return total;
    }
}