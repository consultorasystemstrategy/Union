package union.union_vr1.Objects;

/**
 * Created by Usuario on 15/01/2015.
 */
public class Precio {

    private int idProducto;
    private int idCategoriaEstablecimiento;
    private Double costoVenta;
    private Double precioUnitario;
    private int valorUnidad;
    private int idAgente;
    private int desde;
    private int hasta;
    private String nombreProducto;
    private int estadoSincronizacion;

    public Precio(int idProducto, int idCategoriaEstablecimiento, Double costoVenta, Double precioUnitario, int valorUnidad, int idAgente, int desde, int hasta, String nombreProducto, int estadoSincronizacion) {
        this.idProducto = idProducto;
        this.idCategoriaEstablecimiento = idCategoriaEstablecimiento;
        this.costoVenta = costoVenta;
        this.precioUnitario = precioUnitario;
        this.valorUnidad = valorUnidad;
        this.idAgente = idAgente;
        this.desde = desde;
        this.hasta = hasta;
        this.nombreProducto = nombreProducto;
        this.estadoSincronizacion = estadoSincronizacion;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdCategoriaEstablecimiento() {
        return idCategoriaEstablecimiento;
    }

    public void setIdCategoriaEstablecimiento(int idCategoriaEstablecimiento) {
        this.idCategoriaEstablecimiento = idCategoriaEstablecimiento;
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

    public int getValorUnidad() {
        return valorUnidad;
    }

    public void setValorUnidad(int valorUnidad) {
        this.valorUnidad = valorUnidad;
    }

    public int getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(int idAgente) {
        this.idAgente = idAgente;
    }

    public int getDesde() {
        return desde;
    }

    public void setDesde(int desde) {
        this.desde = desde;
    }

    public int getHasta() {
        return hasta;
    }

    public void setHasta(int hasta) {
        this.hasta = hasta;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public int getEstadoSincronizacion() {
        return estadoSincronizacion;
    }

    public void setEstadoSincronizacion(int estadoSincronizacion) {
        this.estadoSincronizacion = estadoSincronizacion;
    }
}
