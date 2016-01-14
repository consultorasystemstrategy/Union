package union.union_vr1.Objects;

/**
 * Created by Kelvin on 14/01/2016.
 */
public class EstablecTemp {
    private String idEstablecTemp;
    private String fecha;
    private int estado;

    public EstablecTemp(String idEstablecTemp, String fecha, int estado) {
        this.idEstablecTemp = idEstablecTemp;
        this.fecha = fecha;
        this.estado = estado;
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
