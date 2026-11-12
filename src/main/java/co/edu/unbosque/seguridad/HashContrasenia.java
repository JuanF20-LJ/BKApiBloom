package co.edu.unbosque.seguridad;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class HashContrasenia {
    private BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();

    public String generarHash(String contraseniaPlana) {
        return codificador.encode(contraseniaPlana);
    }

    public boolean verificarContrasenia(String contraseniaPlana, String hashGuardado) {
        return codificador.matches(contraseniaPlana, hashGuardado);
    }
}
