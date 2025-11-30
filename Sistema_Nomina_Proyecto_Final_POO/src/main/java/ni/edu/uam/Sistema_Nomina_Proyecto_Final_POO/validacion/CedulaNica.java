package ni.edu.uam.Sistema_Nomina_Proyecto_Final_POO.validacion;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CedulaNicaValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface CedulaNica {
    String message() default "Cédula nicaragüense inválida";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
