package co.edu.unbosque.entity.postgres;

import java.io.Serializable;
import java.util.Objects;

public class CodigoVerificacionId implements Serializable {
    private Long idCodigo;

    private Long usuaria;

    public CodigoVerificacionId() {
    }

    public Long getIdCodigo() {
        return idCodigo;
    }

    public void setIdCodigo(Long idCodigo) {
        this.idCodigo = idCodigo;
    }

    public Long getUsuaria() {
        return usuaria;
    }

    public void setUsuaria(Long usuaria) {
        this.usuaria = usuaria;
    }

    public CodigoVerificacionId(Long idCodigo, Long usuaria) {
        this.idCodigo = idCodigo;
        this.usuaria = usuaria;
    }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) {
            return true;
        }
        if (!(otro instanceof CodigoVerificacionId)) {
            return false;
        }
        CodigoVerificacionId llave = (CodigoVerificacionId) otro;
        return Objects.equals(idCodigo, llave.idCodigo) && Objects.equals(usuaria, llave.usuaria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCodigo, usuaria);
    }
}
