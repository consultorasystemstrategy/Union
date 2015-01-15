package union.union_vr1.Objects;

/**
 * Created by Usuario on 15/01/2015.
 */
public class ComprobanteVenta {

    private int idEstablecimiento;
    private int idTipoDocumento;
    private int idFormaPago;
    private int idTipoVenta;
    private String codigoErp;
    private String serie;
    private String numeroDocumento;
    private Double baseImponible;
    private Double igv;
    private Double total;
    private String fechaDoc;
    private String horaDoc;
    private int estadoComprobante;
    private int estadoConexion;
    private int idAgente;
    private int estadoSincronizado;

    public ComprobanteVenta(int idEstablecimiento, int idTipoDocumento, int idFormaPago, int idTipoVenta, String codigoErp, String serie, String numeroDocumento, Double baseImponible, Double igv, Double total, String fechaDoc, String horaDoc, int estadoComprobante, int estadoConexion, int idAgente, int estadoSincronizado) {
        this.idEstablecimiento = idEstablecimiento;
        this.idTipoDocumento = idTipoDocumento;
        this.idFormaPago = idFormaPago;
        this.idTipoVenta = idTipoVenta;
        this.codigoErp = codigoErp;
        this.serie = serie;
        this.numeroDocumento = numeroDocumento;
        this.baseImponible = baseImponible;
        this.igv = igv;
        this.total = total;
        this.fechaDoc = fechaDoc;
        this.horaDoc = horaDoc;
        this.estadoComprobante = estadoComprobante;
        this.estadoConexion = estadoConexion;
        this.idAgente = idAgente;
        this.estadoSincronizado = estadoSincronizado;
    }

    public int getIdEstablecimiento() {
        return idEstablecimiento;
    }

    public void setIdEstablecimiento(int idEstablecimiento) {
        this.idEstablecimiento = idEstablecimiento;
    }

    public int getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(int idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public int getIdFormaPago() {
        return idFormaPago;
    }

    public void setIdFormaPago(int idFormaPago) {
        this.idFormaPago = idFormaPago;
    }

    public int getIdTipoVenta() {
        return idTipoVenta;
    }

    public void setIdTipoVenta(int idTipoVenta) {
        this.idTipoVenta = idTipoVenta;
    }

    public String getCodigoErp() {
        return codigoErp;
    }

    public void setCodigoErp(String codigoErp) {
        this.codigoErp = codigoErp;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public Double getBaseImponible() {
        return baseImponible;
    }

    public void setBaseImponible(Double baseImponible) {
        this.baseImponible = baseImponible;
    }

    public Double getIgv() {
        return igv;
    }

    public void setIgv(Double igv) {
        this.igv = igv;
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

    public int getEstadoConexion() {
        return estadoConexion;
    }

    public void setEstadoConexion(int estadoConexion) {
        this.estadoConexion = estadoConexion;
    }

    public int getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(int idAgente) {
        this.idAgente = idAgente;
    }

    public int getEstadoSincronizado() {
        return estadoSincronizado;
    }

    public void setEstadoSincronizado(int estadoSincronizado) {
        this.estadoSincronizado = estadoSincronizado;
    }
}
