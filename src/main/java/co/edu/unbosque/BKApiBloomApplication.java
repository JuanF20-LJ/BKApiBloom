package co.edu.unbosque;

import co.edu.unbosque.seguridad.DatosIniciales;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BKApiBloomApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext contexto = SpringApplication.run(BKApiBloomApplication.class, args);
        contexto.getBean(DatosIniciales.class).cargarDatosPrueba();
    }

    @Bean
    ModelMapper getModelMapper() {
        return new ModelMapper();
    }
}
