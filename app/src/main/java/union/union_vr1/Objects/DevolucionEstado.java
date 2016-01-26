package union.union_vr1.Objects;

/**
 * Created by Usuario on 25/01/2016.
 */
public class DevolucionEstado {

    private int agenteId;
    private int estado;
    private String fecha;


    public DevolucionEstado() {
    }

    public DevolucionEstado(int agenteId, int estado, String fecha) {
        this.agenteId = agenteId;
        this.estado = estado;
        this.fecha = fecha;
    }

    public int getAgenteId() {
        return agenteId;
    }

    public void setAgenteId(int agenteId) {
        this.agenteId = agenteId;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
