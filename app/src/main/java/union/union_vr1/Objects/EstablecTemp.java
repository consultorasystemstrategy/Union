package union.union_vr1.Objects;

/**
 * Created by Kelvin on 14/01/2016.
 */
public class EstablecTemp {
    private String idEstablecTemp;
    private String fecha;
    private int estado;
    private int idAgente;
    private int idEstablecimientoSID;
    private String observacion;

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

    public int getIdEstablecimientoSID() {
        return idEstablecimientoSID;
    }

    public void setIdEstablecimientoSID(int idEstablecimientoSID) {
        this.idEstablecimientoSID = idEstablecimientoSID;
    }

    public EstablecTemp(String idEstablecTemp, String fecha, int estado, int idAgente, int idEstablecimientoSID, String observacion) {
        this.idEstablecTemp = idEstablecTemp;
        this.fecha = fecha;
        this.estado = estado;
        this.idAgente = idAgente;
        this.idEstablecimientoSID = idEstablecimientoSID;
        this.observacion = observacion;
    }

    public EstablecTemp() {
    }

    public String getIdEstablecTemp() {
        return idEstablecTemp;
    }

    public void setIdEstablecTemp(String idEstablecTemp) {
        this.idEstablecTemp = idEstablecTemp;
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
