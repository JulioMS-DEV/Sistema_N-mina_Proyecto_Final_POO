package ni.edu.uam.Sistema_Nomina_Proyecto_Final_POO.acciones;

import org.openxava.actions.JasperReportBaseAction;
import org.openxava.util.DataSourceConnectionProvider;
import net.sf.jasperreports.engine.JRDataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class ImprimirEmpleadosAction extends JasperReportBaseAction {

    @Override
    protected String getJRXML() {
        return "reports/empleados.jrxml";
    }

    // Devolvemos NULL para que Jasper ignore los datos de la vista
    @Override
    protected JRDataSource getDataSource() throws Exception {
        return null;
    }

    @Override
    protected Map getParameters() throws Exception {
        Map parameters = new HashMap();

        // CORRECCIÓN: Usamos 'new' en lugar de '.create()'
        Connection con = new DataSourceConnectionProvider().getConnection();

        // Pasamos la conexión a JasperReports
        parameters.put("REPORT_CONNECTION", con);

        return parameters;
    }
}