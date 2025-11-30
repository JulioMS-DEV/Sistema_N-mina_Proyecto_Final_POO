package ni.edu.uam.Sistema_Nomina_Proyecto_Final_POO.validacion;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class CedulaNicaValidator implements ConstraintValidator<CedulaNica, String> {

    private static final Pattern CEDULA_PATTERN = Pattern.compile("^\\d{3}-\\d{6}-\\d{4}[A-HJ-NP-RT-Y]$");
    private static final String LETRAS_VALIDAS = "ABCDEFGHJKLMNPRSTUVWXYZ";

    @Override
    public void initialize(CedulaNica constraintAnnotation) {
    }

    @Override
    public boolean isValid(String cedula, ConstraintValidatorContext context) {
        if (cedula == null || cedula.isEmpty()) {
            return true; // No valida campos nulos o vacíos
        }

        if (!CEDULA_PATTERN.matcher(cedula).matches()) {
            return false; // No cumple con el formato XXX-XXXXXX-XXXXL
        }

        String digitos = cedula.replace("-", "").substring(0, 13);
        char letraIngresada = cedula.charAt(15);

        try {
            long numero = Long.parseLong(digitos);
            long residuo = numero % 23;
            char letraCalculada = LETRAS_VALIDAS.charAt((int) residuo);

            return letraIngresada == letraCalculada;
        } catch (NumberFormatException e) {
            return false; // No debería ocurrir si el patrón es correcto
        }
    }
}
