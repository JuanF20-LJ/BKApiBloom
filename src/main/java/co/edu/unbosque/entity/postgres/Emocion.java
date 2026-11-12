package co.edu.unbosque.entity.postgres;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "emocion")
public class Emocion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idemocion")
    private Long idEmocion;

    @Column(name = "nombre", nullable = false, length = 60)
    private String nombre;

    @Column(name = "categoria", nullable = false, length = 10)
    private String categoria;

    public Emocion() {
    }

    public Long getIdEmocion() {
        return idEmocion;
    }

    public void setIdEmocion(Long idEmocion) {
        this.idEmocion = idEmocion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
