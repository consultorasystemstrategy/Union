package union.union_vr1.Objects;

/**
 * Created by Usuario on 15/01/2015.
 */
public class InformeGastos {

    private int idTipoGasto;
    private int idProcedenciaGasto;
    private int idTipoDocumento;
    private String nombreTipoGasto;
    private Double subtotal;
    private Double igv;
    private Double total;
    private String fecha;
    private String hora;
    private int estado;
    private String referencia;
    private int idAgente;
    private int estadoSincronizacion;

    public InformeGastos(int idTipoGasto, int idProcedenciaGasto, int idTipoDocumento, String nombreTipoGasto, Double subtotal, Double igv, Double total, String fecha, String hora, int estado, String referencia, int idAgente, int estadoSincronizacion) {
        this.idTipoGasto = idTipoGasto;
        this.idProcedenciaGasto = idProcedenciaGasto;
        this.idTipoDocumento = idTipoDocumento;
        this.nombreTipoGasto = nombreTipoGasto;
        this.subtotal = subtotal;
        this.igv = igv;
        this.total = total;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
        this.referencia = referencia;
        this.idAgente = idAgente;
        this.estadoSincronizacion = estadoSincronizacion;
    }

    public int getIdTipoGasto() {
        return idTipoGasto;
    }

    public void setIdTipoGasto(int idTipoGasto) {
        this.idTipoGasto = idTipoGasto;
    }

    public int getIdProcedenciaGasto() {
        return idProcedenciaGasto;
    }

    public void setIdProcedenciaGasto(int idProcedenciaGasto) {
        this.idProcedenciaGasto = idProcedenciaGasto;
    }

    public int getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(int idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public String getNombreTipoGasto() {
        return nombreTipoGasto;
    }

    public void setNombreTipoGasto(String nombreTipoGasto) {
        this.nombreTipoGasto = nombreTipoGasto;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
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
