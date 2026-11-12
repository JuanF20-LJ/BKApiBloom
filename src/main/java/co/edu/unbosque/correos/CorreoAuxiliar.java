package co.edu.unbosque.correos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class CorreoAuxiliar {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String remitente;

    public boolean enviarCodigoVerificacion(String correoDestino, String apodo, String codigo, int minutosExpiracion) {
        String asunto = "Bloom - Tu código de verificación";
        String cuerpo = "Hola " + apodo + ",\n\n"
                + "Tu código de verificación es: " + codigo + "\n\n"
                + "Vence en " + minutosExpiracion + " minutos y solo se puede usar una vez.\n"
                + "Si no fuiste tú, ignora este correo.\n\n"
                + "Equipo Bloom <3";
        return enviarCorreo(correoDestino, asunto, cuerpo);
    }

    public boolean enviarCorreo(String correoDestino, String asunto, String cuerpo) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom(remitente);
        mensaje.setTo(correoDestino);
        mensaje.setSubject(asunto);
        mensaje.setText(cuerpo);

        javaMailSender.send(mensaje);
        return true;
    }
}
