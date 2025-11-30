package ni.edu.uam.Sistema_Nomina_Proyecto_Final_POO.acciones;

import ni.edu.uam.Sistema_Nomina_Proyecto_Final_POO.modelo.Pago;
import org.openxava.actions.JasperReportBaseAction;
import org.openxava.util.DataSourceConnectionProvider;
import org.openxava.model.MapFacade;
import net.sf.jasperreports.engine.JRDataSource;
import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import java.math.BigDecimal;
import java.text.DecimalFormat; // Importamos esto

public class ImprimirPagoAction extends JasperReportBaseAction {

    @Override
    protected String getJRXML() {
        return "reports/pago_individual.jrxml";
    }

    @Override
    protected JRDataSource getDataSource() throws Exception {
        return null;
    }

    @Override
    protected Map getParameters() throws Exception {
        Map parameters = new HashMap();
        Map key = getView().getKeyValues();
        Pago pagoReal = (Pago) MapFacade.findEntity("Pago", key);

        parameters.put("ID_PAGO", pagoReal.getId());

        // --- CAMBIO CLAVE: FORMATEAMOS EN JAVA ---
        BigDecimal monto = pagoReal.getMontoTotal();
        if (monto == null) monto = BigDecimal.ZERO;

        // Creamos el formateador aquí en Java que es seguro
        DecimalFormat df = new DecimalFormat("#,##0.00");
        String textoBonito = "C$ " + df.format(monto);

        // Enviamos un STRING, no un número
        parameters.put("MONTO_FORMATO_TEXTO", textoBonito);
        // ----------------------------------------

        Connection con = new DataSourceConnectionProvider().getConnection();
        parameters.put("REPORT_CONNECTION", con);

        return parameters;
    }
}