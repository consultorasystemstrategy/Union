package union.union_vr1.Objects;

/**
 * Created by Usuario on 13/01/2016.
 */
public class NuevoEstablecimiento {

    private String nroDoc;
    private String fecha;
    private int estado;

    private int idAgente;

    private String observacion;

    public NuevoEstablecimiento() {
    }

    public NuevoEstablecimiento(String nroDoc, String fecha, int estado, int idAgente, String observacion) {
        this.nroDoc = nroDoc;
        this.fecha = fecha;
        this.estado = estado;
        this.idAgente = idAgente;
        this.observacion = observacion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public int getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(int idAgente) {
        this.idAgente = idAgente;
    }


    public String getNroDoc() {
        return nroDoc;
    }

    public void setNroDoc(String nroDoc) {
        this.nroDoc = nroDoc;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
