package union.union_vr1.Objects;

/**
 * Created by Usuario on 15/01/2015.
 */
public class HistorialVentaDetalles {

    private int idDetalle;
    private int idComprobante;
    private int idEstablecimiento;
    private int idProducto;
    private int idTipoPersona;
    private int orden;
    private String comprobante;
    private String nombreEstablecimiento;
    private String nombreProducto;
    private int cantidad;
    private Double importe;
    private String fecha;
    private String hora;
    private int valorUnidad;
    private int categoriaOperacion;
    private int formaOperacion;
    private int cantidadOperacion;
    private Double importeOperacion;
    private String fechaOperacion;
    private String horaOperacion;
    private String lote;
    private String lugarRegistro;
    private int estado;
    private int idAgente;
    private int cantidadOperacionesDevuelto;
    private int categoriaOperacionesDevuelto;
    private Double  importeOperacionesDevuelto;
    private String fechaOperacionesDevuelto;
    private String horaOperacionesDevuelto;
    private int estadoSincronizacion;

    public HistorialVentaDetalles(int idDetalle, int idComprobante, int idEstablecimiento, int idProducto, int idTipoPersona, int orden, String comprobante, String nombreEstablecimiento, String nombreProducto, int cantidad, Double importe, String fecha, String hora, int valorUnidad, int categoriaOperacion, int formaOperacion, int cantidadOperacion, Double importeOperacion, String fechaOperacion, String horaOperacion, String lote, String lugarRegistro, int estado, int idAgente, int cantidadOperacionesDevuelto, int categoriaOperacionesDevuelto, Double importeOperacionesDevuelto, String fechaOperacionesDevuelto, String horaOperacionesDevuelto, int estadoSincronizacion) {
        this.idDetalle = idDetalle;
        this.idComprobante = idComprobante;
        this.idEstablecimiento = idEstablecimiento;
        this.idProducto = idProducto;
        this.idTipoPersona = idTipoPersona;
        this.orden = orden;
        this.comprobante = comprobante;
        this.nombreEstablecimiento = nombreEstablecimiento;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.importe = importe;
        this.fecha = fecha;
        this.hora = hora;
        this.valorUnidad = valorUnidad;
        this.categoriaOperacion = categoriaOperacion;
        this.formaOperacion = formaOperacion;
        this.cantidadOperacion = cantidadOperacion;
        this.importeOperacion = importeOperacion;
        this.fechaOperacion = fechaOperacion;
        this.horaOperacion = horaOperacion;
        this.lote = lote;
        this.lugarRegistro = lugarRegistro;
        this.estado = estado;
        this.idAgente = idAgente;
        this.cantidadOperacionesDevuelto = cantidadOperacionesDevuelto;
        this.categoriaOperacionesDevuelto = categoriaOperacionesDevuelto;
        this.importeOperacionesDevuelto = importeOperacionesDevuelto;
        this.fechaOperacionesDevuelto = fechaOperacionesDevuelto;
        this.horaOperacionesDevuelto = horaOperacionesDevuelto;
        this.estadoSincronizacion = estadoSincronizacion;
    }

    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
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

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdTipoPersona() {
        return idTipoPersona;
    }

    public void setIdTipoPersona(int idTipoPersona) {
        this.idTipoPersona = idTipoPersona;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public String getComprobante() {
        return comprobante;
    }

    public void setComprobante(String comprobante) {
        this.comprobante = comprobante;
    }

    public String getNombreEstablecimiento() {
        return nombreEstablecimiento;
    }

    public void setNombreEstablecimiento(String nombreEstablecimiento) {
        this.nombreEstablecimiento = nombreEstablecimiento;
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

    public int getValorUnidad() {
        return valorUnidad;
    }

    public void setValorUnidad(int valorUnidad) {
        this.valorUnidad = valorUnidad;
    }

    public int getCategoriaOperacion() {
        return categoriaOperacion;
    }

    public void setCategoriaOperacion(int categoriaOperacion) {
        this.categoriaOperacion = categoriaOperacion;
    }

    public int getFormaOperacion() {
        return formaOperacion;
    }

    public void setFormaOperacion(int formaOperacion) {
        this.formaOperacion = formaOperacion;
    }

    public int getCantidadOperacion() {
        return cantidadOperacion;
    }

    public void setCantidadOperacion(int cantidadOperacion) {
        this.cantidadOperacion = cantidadOperacion;
    }

    public Double getImporteOperacion() {
        return importeOperacion;
    }

    public void setImporteOperacion(Double importeOperacion) {
        this.importeOperacion = importeOperacion;
    }

    public String getFechaOperacion() {
        return fechaOperacion;
    }

    public void setFechaOperacion(String fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }

    public String getHoraOperacion() {
        return horaOperacion;
    }

    public void setHoraOperacion(String horaOperacion) {
        this.horaOperacion = horaOperacion;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getLugarRegistro() {
        return lugarRegistro;
    }

    public void setLugarRegistro(String lugarRegistro) {
        this.lugarRegistro = lugarRegistro;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(int idAgente) {
        this.idAgente = idAgente;
    }

    public int getCantidadOperacionesDevuelto() {
        return cantidadOperacionesDevuelto;
    }

    public void setCantidadOperacionesDevuelto(int cantidadOperacionesDevuelto) {
        this.cantidadOperacionesDevuelto = cantidadOperacionesDevuelto;
    }

    public int getCategoriaOperacionesDevuelto() {
        return categoriaOperacionesDevuelto;
    }

    public void setCategoriaOperacionesDevuelto(int categoriaOperacionesDevuelto) {
        this.categoriaOperacionesDevuelto = categoriaOperacionesDevuelto;
    }

    public Double getImporteOperacionesDevuelto() {
        return importeOperacionesDevuelto;
    }

    public void setImporteOperacionesDevuelto(Double importeOperacionesDevuelto) {
        this.importeOperacionesDevuelto = importeOperacionesDevuelto;
    }

    public String getFechaOperacionesDevuelto() {
        return fechaOperacionesDevuelto;
    }

    public void setFechaOperacionesDevuelto(String fechaOperacionesDevuelto) {
        this.fechaOperacionesDevuelto = fechaOperacionesDevuelto;
    }

    public String getHoraOperacionesDevuelto() {
        return horaOperacionesDevuelto;
    }

    public void setHoraOperacionesDevuelto(String horaOperacionesDevuelto) {
        this.horaOperacionesDevuelto = horaOperacionesDevuelto;
    }

    public int getEstadoSincronizacion() {
        return estadoSincronizacion;
    }

    public void setEstadoSincronizacion(int estadoSincronizacion) {
        this.estadoSincronizacion = estadoSincronizacion;
    }
}
