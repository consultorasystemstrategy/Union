package union.union_vr1.Objects;

/**
 * Created by Usuario on 15/01/2015.
 */
public class EventoEstablecimiento {

    private int idEstablecimiento;
    private int idCategoriaEstablecimiento;
    private int tipoDocCliente;
    private int estadoAtencion;
    private String nombreEstablecimiento;
    private String nombreCliente;
    private String docCliente;
    private int orden;
    private int surtidoStockAnterior;
    private int surtidoVentaAnterior;
    private Double montoCredito;
    private int diasCredito;
    private int idEstadoNoAtencion;
    private String estadoNoAtencionComentario;
    private int idAgente;
    private int estadoSincronizacion;
    private String codigoBarras;

    public EventoEstablecimiento(int idEstablecimiento, int idCategoriaEstablecimiento, int tipoDocCliente, int estadoAtencion, String nombreEstablecimiento, String nombreCliente, String docCliente, int orden, int surtidoStockAnterior, int surtidoVentaAnterior, Double montoCredito, int diasCredito, int idEstadoNoAtencion, String estadoNoAtencionComentario, int idAgente, int estadoSincronizacion, String codigoBarras) {
        this.idEstablecimiento = idEstablecimiento;
        this.idCategoriaEstablecimiento = idCategoriaEstablecimiento;
        this.tipoDocCliente = tipoDocCliente;
        this.estadoAtencion = estadoAtencion;
        this.nombreEstablecimiento = nombreEstablecimiento;
        this.nombreCliente = nombreCliente;
        this.docCliente = docCliente;
        this.orden = orden;
        this.surtidoStockAnterior = surtidoStockAnterior;
        this.surtidoVentaAnterior = surtidoVentaAnterior;
        this.montoCredito = montoCredito;
        this.diasCredito = diasCredito;
        this.idEstadoNoAtencion = idEstadoNoAtencion;
        this.estadoNoAtencionComentario = estadoNoAtencionComentario;
        this.idAgente = idAgente;
        this.estadoSincronizacion = estadoSincronizacion;
        this.codigoBarras = codigoBarras;
    }


    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }
    public int getIdEstablecimiento() {
        return idEstablecimiento;
    }

    public void setIdEstablecimiento(int idEstablecimiento) {
        this.idEstablecimiento = idEstablecimiento;
    }

    public int getIdCategoriaEstablecimiento() {
        return idCategoriaEstablecimiento;
    }

    public void setIdCategoriaEstablecimiento(int idCategoriaEstablecimiento) {
        this.idCategoriaEstablecimiento = idCategoriaEstablecimiento;
    }

    public int getTipoDocCliente() {
        return tipoDocCliente;
    }

    public void setTipoDocCliente(int tipoDocCliente) {
        this.tipoDocCliente = tipoDocCliente;
    }

    public int getEstadoAtencion() {
        return estadoAtencion;
    }

    public void setEstadoAtencion(int estadoAtencion) {
        this.estadoAtencion = estadoAtencion;
    }

    public String getNombreEstablecimiento() {
        return nombreEstablecimiento;
    }

    public void setNombreEstablecimiento(String nombreEstablecimiento) {
        this.nombreEstablecimiento = nombreEstablecimiento;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getDocCliente() {
        return docCliente;
    }

    public void setDocCliente(String docCliente) {
        this.docCliente = docCliente;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public int getSurtidoStockAnterior() {
        return surtidoStockAnterior;
    }

    public void setSurtidoStockAnterior(int surtidoStockAnterior) {
        this.surtidoStockAnterior = surtidoStockAnterior;
    }

    public int getSurtidoVentaAnterior() {
        return surtidoVentaAnterior;
    }

    public void setSurtidoVentaAnterior(int surtidoVentaAnterior) {
        this.surtidoVentaAnterior = surtidoVentaAnterior;
    }

    public Double getMontoCredito() {
        return montoCredito;
    }

    public void setMontoCredito(Double montoCredito) {
        this.montoCredito = montoCredito;
    }

    public int getDiasCredito() {
        return diasCredito;
    }

    public void setDiasCredito(int diasCredito) {
        this.diasCredito = diasCredito;
    }

    public int getIdEstadoNoAtencion() {
        return idEstadoNoAtencion;
    }

    public void setIdEstadoNoAtencion(int idEstadoNoAtencion) {
        this.idEstadoNoAtencion = idEstadoNoAtencion;
    }

    public String getEstadoNoAtencionComentario() {
        return estadoNoAtencionComentario;
    }

    public void setEstadoNoAtencionComentario(String estadoNoAtencionComentario) {
        this.estadoNoAtencionComentario = estadoNoAtencionComentario;
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
