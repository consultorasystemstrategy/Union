package union.union_vr1.Objects;

/**
 * Created by Usuario on 15/01/2015.
 */
public class ComprobanteVentaDetalle {

    private int idComprobante;
    private int idEstablecimiento;
    private int idAgente;
    private int idProducto;
    private String nombreProducto;
    private int cantidad;
    private Double importe;
    private Double costoVenta;
    private Double precioUnitario;
    private String promedioAnterior;
    private String devuelto;
    private int valorUnidad;
    private int estadoSincronizacion;

    public ComprobanteVentaDetalle(int idComprobante, int idEstablecimiento, int idAgente, int idProducto, String nombreProducto, int cantidad, Double importe, Double costoVenta, Double precioUnitario, String promedioAnterior, String devuelto, int valorUnidad, int estadoSincronizacion) {
        this.idComprobante = idComprobante;
        this.idEstablecimiento = idEstablecimiento;
        this.idAgente = idAgente;
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.importe = importe;
        this.costoVenta = costoVenta;
        this.precioUnitario = precioUnitario;
        this.promedioAnterior = promedioAnterior;
        this.devuelto = devuelto;
        this.valorUnidad = valorUnidad;
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

    public int getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(int idAgente) {
        this.idAgente = idAgente;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public Double getCostoVenta() {
        return costoVenta;
    }

    public void setCostoVenta(Double costoVenta) {
        this.costoVenta = costoVenta;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public String getPromedioAnterior() {
        return promedioAnterior;
    }

    public void setPromedioAnterior(String promedioAnterior) {
        this.promedioAnterior = promedioAnterior;
    }

    public String getDevuelto() {
        return devuelto;
    }

    public void setDevuelto(String devuelto) {
        this.devuelto = devuelto;
    }

    public int getValorUnidad() {
        return valorUnidad;
    }

    public void setValorUnidad(int valorUnidad) {
        this.valorUnidad = valorUnidad;
    }

    public int getEstadoSincronizacion() {
        return estadoSincronizacion;
    }

    public void setEstadoSincronizacion(int estadoSincronizacion) {
        this.estadoSincronizacion = estadoSincronizacion;
    }
}
