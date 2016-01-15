package union.union_vr1.Objects;

/**
 * Created by Kelvin on 14/01/2016.
 */
public class EstablecTemp {
    private String idEstablecTemp;
    private String fecha;
    private int estado;
    private int idAgente;

    public int getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(int idAgente) {
        this.idAgente = idAgente;
    }

    public EstablecTemp(String idEstablecTemp, String fecha, int estado, int idAgente) {
        this.idEstablecTemp = idEstablecTemp;
        this.fecha = fecha;
        this.estado = estado;
        this.idAgente = idAgente;
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
