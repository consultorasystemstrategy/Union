package union.union_vr1.Objects;

/**
 * Created by Usuario on 08/02/2016.
 */
public class Exportaciones {

    private int agenteId;
    private int estado;
    private int idComprobante;
    private int liquidacion;
    private String fecha;

    public Exportaciones() {
    }

    public Exportaciones(int agenteId, int estado, int idComprobante, int liquidacion, String fecha) {
        this.agenteId = agenteId;
        this.estado = estado;
        this.idComprobante = idComprobante;
        this.liquidacion = liquidacion;
        this.fecha = fecha;
    }

    public int getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(int liquidacion) {
        this.liquidacion = liquidacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
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

    public int getIdComprobante() {
        return idComprobante;
    }

    public void setIdComprobante(int idComprobante) {
        this.idComprobante = idComprobante;
    }
}
