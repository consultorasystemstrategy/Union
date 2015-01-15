package union.union_vr1.Objects;

/**
 * Created by Usuario on 15/01/2015.
 */
public class ComprobanteCobro {

    private int idEstablecimiento;
    private int idComprobante;
    private int idPlanPago;
    private int idPlanPagoDetalle;
    private String descTipoDoc;
    private String doc;
    private String fechaProgramada;
    private Double montoPagar;
    private String fechaCobro;
    private String horaCobro;
    private Double montoCobrado;
    private int estadoCobro;
    private int idAgente;
    private int idFormaCobro;
    private String lugarRegistro;
    private int estadoSincronizado;

    public ComprobanteCobro(int idEstablecimiento, int idComprobante, int idPlanPago, int idPlanPagoDetalle, String descTipoDoc, String doc, String fechaProgramada, Double montoPagar, String fechaCobro, String horaCobro, Double montoCobrado, int estadoCobro, int idAgente, int idFormaCobro, String lugarRegistro, int estadoSincronizado) {
        this.idEstablecimiento = idEstablecimiento;
        this.idComprobante = idComprobante;
        this.idPlanPago = idPlanPago;
        this.idPlanPagoDetalle = idPlanPagoDetalle;
        this.descTipoDoc = descTipoDoc;
        this.doc = doc;
        this.fechaProgramada = fechaProgramada;
        this.montoPagar = montoPagar;
        this.fechaCobro = fechaCobro;
        this.horaCobro = horaCobro;
        this.montoCobrado = montoCobrado;
        this.estadoCobro = estadoCobro;
        this.idAgente = idAgente;
        this.idFormaCobro = idFormaCobro;
        this.lugarRegistro = lugarRegistro;
        this.estadoSincronizado = estadoSincronizado;
    }


    public int getIdEstablecimiento() {
        return idEstablecimiento;
    }

    public void setIdEstablecimiento(int idEstablecimiento) {
        this.idEstablecimiento = idEstablecimiento;
    }

    public int getIdComprobante() {
        return idComprobante;
    }

    public void setIdComprobante(int idComprobante) {
        this.idComprobante = idComprobante;
    }

    public int getIdPlanPago() {
        return idPlanPago;
    }

    public void setIdPlanPago(int idPlanPago) {
        this.idPlanPago = idPlanPago;
    }

    public int getIdPlanPagoDetalle() {
        return idPlanPagoDetalle;
    }

    public void setIdPlanPagoDetalle(int idPlanPagoDetalle) {
        this.idPlanPagoDetalle = idPlanPagoDetalle;
    }

    public String getDescTipoDoc() {
        return descTipoDoc;
    }

    public void setDescTipoDoc(String descTipoDoc) {
        this.descTipoDoc = descTipoDoc;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public String getFechaProgramada() {
        return fechaProgramada;
    }

    public void setFechaProgramada(String fechaProgramada) {
        this.fechaProgramada = fechaProgramada;
    }

    public Double getMontoPagar() {
        return montoPagar;
    }

    public void setMontoPagar(Double montoPagar) {
        this.montoPagar = montoPagar;
    }

    public String getFechaCobro() {
        return fechaCobro;
    }

    public void setFechaCobro(String fechaCobro) {
        this.fechaCobro = fechaCobro;
    }

    public String getHoraCobro() {
        return horaCobro;
    }

    public void setHoraCobro(String horaCobro) {
        this.horaCobro = horaCobro;
    }

    public Double getMontoCobrado() {
        return montoCobrado;
    }

    public void setMontoCobrado(Double montoCobrado) {
        this.montoCobrado = montoCobrado;
    }

    public int getEstadoCobro() {
        return estadoCobro;
    }

    public void setEstadoCobro(int estadoCobro) {
        this.estadoCobro = estadoCobro;
    }

    public int getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(int idAgente) {
        this.idAgente = idAgente;
    }

    public int getIdFormaCobro() {
        return idFormaCobro;
    }

    public void setIdFormaCobro(int idFormaCobro) {
        this.idFormaCobro = idFormaCobro;
    }

    public String getLugarRegistro() {
        return lugarRegistro;
    }

    public void setLugarRegistro(String lugarRegistro) {
        this.lugarRegistro = lugarRegistro;
    }

    public int getEstadoSincronizado() {
        return estadoSincronizado;
    }

    public void setEstadoSincronizado(int estadoSincronizado) {
        this.estadoSincronizado = estadoSincronizado;
    }
}
