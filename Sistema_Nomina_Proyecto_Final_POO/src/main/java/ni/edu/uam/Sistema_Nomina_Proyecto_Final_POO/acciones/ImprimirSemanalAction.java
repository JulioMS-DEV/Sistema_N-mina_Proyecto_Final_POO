package ni.edu.uam.Sistema_Nomina_Proyecto_Final_POO.acciones;

import ni.edu.uam.Sistema_Nomina_Proyecto_Final_POO.modelo.Pago;
import org.openxava.actions.JasperReportBaseAction;
import org.openxava.model.MapFacade;
import org.openxava.tab.Tab;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ImprimirSemanalAction extends JasperReportBaseAction {

    @Inject
    private Tab tab;

    // Guardaremos el total aquí para pasarlo luego a los parámetros
    private String totalGeneralFormateado = "C$ 0.00";

    @Override
    protected String getJRXML() {
        return "reports/pagos_semanal.jrxml";
    }

    @Override
    protected JRDataSource getDataSource() throws Exception {
        List<Map<String, ?>> listaDatos = new ArrayList<>();
        Map[] selectedKeys = tab.getSelectedKeys();

        if (selectedKeys == null || selectedKeys.length == 0) {
            addMessage("Por favor, selecciona los pagos que deseas incluir en el reporte (checkbox).");
            return new JRMapCollectionDataSource(new ArrayList<>());
        }

        DecimalFormat df = new DecimalFormat("C$ #,##0.00");
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // 1. VARIABLE ACUMULADORA
        BigDecimal sumaTotal = BigDecimal.ZERO;

        for (Map key : selectedKeys) {
            Pago pago = (Pago) MapFacade.findEntity("Pago", key);

            BigDecimal monto = pago.getMontoTotal();
            if (monto == null) monto = BigDecimal.ZERO;

            // 2. SUMAR AL TOTAL
            sumaTotal = sumaTotal.add(monto);

            Map<String, Object> fila = new HashMap<>();
            fila.put("empleado", pago.getEmpleado().getNombres() + " " + pago.getEmpleado().getApellidos());
            fila.put("cedula", pago.getEmpleado().getCedula());
            fila.put("fecha", pago.getFechaPago().format(dateFmt));
            fila.put("monto", df.format(monto));

            listaDatos.add(fila);
        }

        // 3. FORMATEAR EL TOTAL FINAL
        this.totalGeneralFormateado = df.format(sumaTotal);

        return new JRMapCollectionDataSource(listaDatos);
    }

    @Override
    protected Map getParameters() throws Exception {
        Map parameters = new HashMap();

        BigDecimal sumaTotal = BigDecimal.ZERO;
        Map[] selectedKeys = tab.getSelectedKeys();

        if (selectedKeys != null) {
            for (Map key : selectedKeys) {
                // Buscamos la entidad para obtener el monto real
                Pago pago = (Pago) MapFacade.findEntity("Pago", key);

                BigDecimal monto = pago.getMontoTotal();
                if (monto != null) {
                    sumaTotal = sumaTotal.add(monto);
                }
            }
        }

        // Formateamos y enviamos
        DecimalFormat df = new DecimalFormat("C$ #,##0.00");
        parameters.put("TXT_TOTAL_GENERAL", df.format(sumaTotal));

        return parameters;
    }

    public Tab getTab() {
        return tab;
    }

    public void setTab(Tab tab) {
        this.tab = tab;
    }
}