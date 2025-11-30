package ni.edu.uam.Sistema_Nomina_Proyecto_Final_POO.acciones;

import ni.edu.uam.Sistema_Nomina_Proyecto_Final_POO.modelo.Pago;
import org.openxava.actions.JasperReportBaseAction;
import org.openxava.model.MapFacade;
import org.openxava.tab.Tab; // 1. IMPORTANTE: Importar Tab
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import javax.inject.Inject; // 2. IMPORTANTE: Para la inyección
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ImprimirSemanalAction extends JasperReportBaseAction {

    // 3. INYECCIÓN DEL TAB (La Lista)
    // Esto le dice a OpenXava: "Dame acceso a la tabla visual"
    @Inject
    private Tab tab;

    @Override
    protected String getJRXML() {
        return "reports/pagos_semanal.jrxml";
    }

    @Override
    protected JRDataSource getDataSource() throws Exception {
        List<Map<String, ?>> listaDatos = new ArrayList<>();

        // 4. USAR EL TAB
        // Ahora sí podemos usar 'tab' porque lo inyectamos arriba
        Map[] selectedKeys = tab.getSelectedKeys();

        if (selectedKeys == null || selectedKeys.length == 0) {
            addMessage("Por favor, selecciona los pagos que deseas incluir en el reporte (checkbox).");
            // Retornamos lista vacía para no romper el reporte
            return new JRMapCollectionDataSource(new ArrayList<>());
        }

        DecimalFormat df = new DecimalFormat("C$ #,##0.00");
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Map key : selectedKeys) {
            // Buscamos la entidad real
            Pago pago = (Pago) MapFacade.findEntity("Pago", key);

            // Calculamos
            BigDecimal monto = pago.getMontoTotal();
            if (monto == null) monto = BigDecimal.ZERO;

            // Llenamos la fila
            Map<String, Object> fila = new HashMap<>();
            fila.put("empleado", pago.getEmpleado().getNombres() + " " + pago.getEmpleado().getApellidos());
            fila.put("cedula", pago.getEmpleado().getCedula());
            fila.put("fecha", pago.getFechaPago().format(dateFmt));

            // Enviamos el monto ya formateado como String
            fila.put("monto", df.format(monto));

            listaDatos.add(fila);
        }

        return new JRMapCollectionDataSource(listaDatos);
    }

    @Override
    protected Map getParameters() throws Exception {
        return new HashMap();
    }

    // 5. Getters y Setters para el Tab (Necesarios para que OpenXava inyecte el valor)
    public Tab getTab() {
        return tab;
    }

    public void setTab(Tab tab) {
        this.tab = tab;
    }
}