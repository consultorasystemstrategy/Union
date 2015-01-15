package union.union_vr1.Objects;

/**
 * Created by Usuario on 15/01/2015.
 */
public class ResumenCaja {

    private int idAgente;
    private String descripcionComprobante;
    private int tipoGI;
    private int cantidad;
    private Double vendido;
    private Double pagado;
    private Double cobrado;
    private String fechaReporte;

    public ResumenCaja(int idAgente, String descripcionComprobante, int tipoGI, int cantidad, Double vendido, Double pagado, Double cobrado, String fechaReporte) {
        this.idAgente = idAgente;
        this.descripcionComprobante = descripcionComprobante;
        this.tipoGI = tipoGI;
        this.cantidad = cantidad;
        this.vendido = vendido;
        this.pagado = pagado;
        this.cobrado = cobrado;
        this.fechaReporte = fechaReporte;
    }

    public int getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(int idAgente) {
        this.idAgente = idAgente;
    }

    public String getDescripcionComprobante() {
        return descripcionComprobante;
    }

    public void setDescripcionComprobante(String descripcionComprobante) {
        this.descripcionComprobante = descripcionComprobante;
    }

    public int getTipoGI() {
        return tipoGI;
    }

    public void setTipoGI(int tipoGI) {
        this.tipoGI = tipoGI;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Double getVendido() {
        return vendido;
    }

    public void setVendido(Double vendido) {
        this.vendido = vendido;
    }

    public Double getPagado() {
        return pagado;
    }

    public void setPagado(Double pagado) {
        this.pagado = pagado;
    }

    public Double getCobrado() {
        return cobrado;
    }

    public void setCobrado(Double cobrado) {
        this.cobrado = cobrado;
    }

    public String getFechaReporte() {
        return fechaReporte;
    }

    public void setFechaReporte(String fechaReporte) {
        this.fechaReporte = fechaReporte;
    }
}
