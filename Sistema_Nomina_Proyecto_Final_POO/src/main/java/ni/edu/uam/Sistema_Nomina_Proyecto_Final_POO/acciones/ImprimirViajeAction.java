package ni.edu.uam.Sistema_Nomina_Proyecto_Final_POO.acciones;

import ni.edu.uam.Sistema_Nomina_Proyecto_Final_POO.modelo.Viaje;
import ni.edu.uam.Sistema_Nomina_Proyecto_Final_POO.modelo.ParticipacionAyudante;
import org.openxava.actions.JasperReportBaseAction;
import org.openxava.model.MapFacade;
import org.openxava.util.DataSourceConnectionProvider;
import net.sf.jasperreports.engine.JRDataSource;

import java.sql.Connection;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class ImprimirViajeAction extends JasperReportBaseAction {

    @Override
    protected String getJRXML() {
        return "reports/detalle_viaje.jrxml";
    }

    // Retornamos NULL porque usamos SQL para la lista de ayudantes
    @Override
    protected JRDataSource getDataSource() throws Exception {
        return null;
    }

    @Override
    protected Map getParameters() throws Exception {
        Map parameters = new HashMap();

        // 1. Obtener el Viaje Real
        Map key = getView().getKeyValues();
        Viaje viaje = (Viaje) MapFacade.findEntity("Viaje", key);

        // 2. Calcular Totales (Chofer + Ayudantes)
        BigDecimal total = BigDecimal.ZERO;

        // Sumamos chofer
        if (viaje.getMontoChofer() != null) {
            total = total.add(viaje.getMontoChofer());
        }

        // Sumamos ayudantes (Recorremos la lista Java para el total general)
        for (ParticipacionAyudante pa : viaje.getAyudantes()) {
            if (pa.getMonto() != null) {
                total = total.add(pa.getMonto());
            }
        }

        // 3. Formateadores
        DecimalFormat df = new DecimalFormat("C$ #,##0.00");
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // 4. Pasar Parámetros (Strings seguros)
        parameters.put("ID_VIAJE", viaje.getId());
        parameters.put("TXT_DESTINO", viaje.getDestino());
        parameters.put("TXT_FECHA", viaje.getFecha().format(dateFmt));
        parameters.put("TXT_CHOFER", viaje.getChofer().getNombres() + " " + viaje.getChofer().getApellidos());

        // Dineros formateados
        parameters.put("TXT_MONTO_CHOFER", df.format(viaje.getMontoChofer()));
        parameters.put("TXT_GRAN_TOTAL", df.format(total));

        // 5. Conexión SQL (Para que el reporte liste a los ayudantes)
        Connection con = new DataSourceConnectionProvider().getConnection();
        parameters.put("REPORT_CONNECTION", con);

        return parameters;
    }
}