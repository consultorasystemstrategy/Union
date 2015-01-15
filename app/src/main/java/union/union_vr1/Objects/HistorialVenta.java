package union.union_vr1.Objects;

/**
 * Created by Usuario on 15/01/2015.
 */
public class HistorialVenta {

    private int idComprobante;
    private int idEstablecimiento;
    private int orden;
    private String serie;
    private int numeroDocumento;
    private Double total;
    private String fechaDoc;
    private String horaDoc;
    private int estadoComprobante;
    private int idAgente;
    private int estadoSincronizacion;

    public HistorialVenta(int idComprobante, int idEstablecimiento, int orden, String serie, int numeroDocumento, Double total, String fechaDoc, String horaDoc, int estadoComprobante, int idAgente, int estadoSincronizacion) {
        this.idComprobante = idComprobante;
        this.idEstablecimiento = idEstablecimiento;
        this.orden = orden;
        this.serie = serie;
        this.numeroDocumento = numeroDocumento;
        this.total = total;
        this.fechaDoc = fechaDoc;
        this.horaDoc = horaDoc;
        this.estadoComprobante = estadoComprobante;
        this.idAgente = idAgente;
        this.estadoSincronizacion = estadoSincronizacion;
    }

    public int getIdComprobante() {
        return idComprobante;
    }

    public void setIdComprobante(int idComprobante) {
        this.idComprobante = idComprobante;
    }

    public int getIdEstablecimiento() {
        return idEstablecimiento;
    }

    public void setIdEstablecimiento(int idEstablecimiento) {
        this.idEstablecimiento = idEstablecimiento;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public int getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(int numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getFechaDoc() {
        return fechaDoc;
    }

    public void setFechaDoc(String fechaDoc) {
        this.fechaDoc = fechaDoc;
    }

    public String getHoraDoc() {
        return horaDoc;
    }

    public void setHoraDoc(String horaDoc) {
        this.horaDoc = horaDoc;
    }

    public int getEstadoComprobante() {
        return estadoComprobante;
    }

    public void setEstadoComprobante(int estadoComprobante) {
        this.estadoComprobante = estadoComprobante;
    }

    public int getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(int idAgente) {
        this.idAgente = idAgente;
    }

    public int getEstadoSincronizacion() {
        return estadoSincronizacion;
    }

    public void setEstadoSincronizacion(int estadoSincronizacion) {
        this.estadoSincronizacion = estadoSincronizacion;
    }

}
