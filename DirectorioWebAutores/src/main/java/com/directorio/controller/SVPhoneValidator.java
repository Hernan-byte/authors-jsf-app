package com.directorio.controller;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.regex.Pattern;

@FacesValidator("svPhoneValidator")
public class SVPhoneValidator implements Validator {

    // Formato: empieza con 2,3,6 o 7 seguido de 3 dígitos, guion, 4 dígitos
    // Ejemplo válido: 2250-5555 / 7891-2345
    private static final Pattern SV_PHONE_PATTERN =
            Pattern.compile("^[2367]\\d{3}-\\d{4}$");

    @Override
    public void validate(FacesContext context, UIComponent component, Object value)
            throws ValidatorException {

        if (value == null || value.toString().trim().isEmpty()) {
            return; // El campo vacío lo maneja @NotNull si se necesita
        }

        String phone = value.toString().trim();

        if (!SV_PHONE_PATTERN.matcher(phone).matches()) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Teléfono inválido",
                    "Debe iniciar con 2, 3, 6 o 7 seguido de 3 dígitos, guion y 4 dígitos. Ej: 2250-5555"
            ));
        }
    }
}
